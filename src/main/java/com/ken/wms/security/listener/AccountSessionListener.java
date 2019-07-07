package com.ken.wms.security.listener;

import com.ken.wms.common.service.Interface.SystemLogService;
import com.ken.wms.dao.AccessRecordMapper;
import com.ken.wms.domain.AccessRecordDO;
import com.ken.wms.exception.SystemLogServiceException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

/**
 * 用户Session监听器
 * 当用户session注销时，记录用户账户登出的时间
 *
 * @author Ken
 * @since 2017/3/4.
 */
@Component
public class AccountSessionListener implements HttpSessionListener, ApplicationContextAware{

    @Autowired
    private SystemLogService systemLogService;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 当session被创建时
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 获取用户的session
        HttpSession session = se.getSession();

        // 判断是否为已经登陆的用户
        // 判断依据是isAuthentication的值
        try {
            String isAuthenticate = (String) session.getAttribute("isAuthenticate");
            if (isAuthenticate != null && isAuthenticate.equals("true")) {
                Integer userID = (Integer) session.getAttribute("userID");
                String userName = (String) session.getAttribute("userName");
                String accessIP = "-";
                systemLogService.insertAccessRecord(userID, userName, accessIP, SystemLogService.ACCESS_TYPE_LOGOUT);
            }
        } catch (SystemLogServiceException e) {
            // do log
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof WebApplicationContext){
            ((WebApplicationContext)applicationContext).getServletContext().addListener(this);
        }else{
            throw new RuntimeException();
        }
    }
}
