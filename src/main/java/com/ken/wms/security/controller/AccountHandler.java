package com.ken.wms.security.controller;

import com.ken.wms.common.service.Interface.SystemLogService;
import com.ken.wms.common.util.Response;
import com.ken.wms.common.util.ResponseUtil;
import com.ken.wms.exception.SystemLogServiceException;
import com.ken.wms.exception.UserAccountServiceException;
import com.ken.wms.security.service.Interface.AccountService;
import com.ken.wms.security.util.CheckCodeGenerator;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 用户账户请求 Handler
 */
@Controller
@RequestMapping("/account")
public class AccountHandler {

    private static Logger log = Logger.getLogger("application");

    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private CheckCodeGenerator checkCodeGenerator;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SystemLogService systemLogService;

    private static final String USER_ID = "id";
    private static final String USER_NAME = "userName";
    private static final String USER_PASSWORD = "password";

    /**
     * 登陆账户
     *
     * @param user 账户信息 传入的密码是前端加密之后的
     * @return 返回一个 Map 对象，其中包含登陆操作的结果
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> login(@RequestBody Map<String, Object> user) {
        // 初始化 Response
        Response response = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;
        String errorMsg = "";

        // 获取当前的用户的 Subject，shiro
        Subject currentUser = SecurityUtils.getSubject();

        // 判断用户是否已经登陆
        if (currentUser != null && !currentUser.isAuthenticated()) {
            String id = (String) user.get(USER_ID);
            String password = (String) user.get(USER_PASSWORD);
            UsernamePasswordToken token = new UsernamePasswordToken(id, password);

            System.out.println(id);
            System.out.println(password);
            System.out.println(token);

            // 执行登陆操作
            try {
                //会调用realms/UserAuthorizingRealm中的doGetAuthenticationInfo方法
                currentUser.login(token);

                // 设置登陆状态并记录
                Session session = currentUser.getSession();
                session.setAttribute("isAuthenticate", "true");

                Integer userID_integer = (Integer) session.getAttribute("userID");
                String userName = (String) session.getAttribute("userName");
                String accessIP = session.getHost();

//                System.out.println(userID_integer);
//                System.out.println(userName);
//                System.out.println(accessIP);
                systemLogService.insertAccessRecord(userID_integer, userName, accessIP, SystemLogService.ACCESS_TYPE_LOGIN);

                result = Response.RESPONSE_RESULT_SUCCESS;
            } catch (UnknownAccountException e) {
                errorMsg = "unknownAccount";
            } catch (IncorrectCredentialsException e) {
                errorMsg = "incorrectCredentials";
            } catch (AuthenticationException e) {
                errorMsg = "authenticationError";
            } catch (SystemLogServiceException e) {
                errorMsg = "ServerError";
            }
        } else {
            errorMsg = "already login";
        }

        // 设置 Response
        response.setResponseResult(result);
        response.setResponseMsg(errorMsg);
        return response.generateResponse();
    }

    /**
     * 注销账户
     *
     * @return 返回一个 Map 对象，键值为 result 的内容代表注销操作的结果，值为 success 或 error
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> logout() {
        // 初始化 Response
        Response response = responseUtil.newResponseInstance();

        Subject currentSubject = SecurityUtils.getSubject();
        if (currentSubject != null && currentSubject.isAuthenticated()) {
            // 执行账户注销操作
            currentSubject.logout();
            response.setResponseResult(Response.RESPONSE_RESULT_SUCCESS);
        } else {
            response.setResponseResult(Response.RESPONSE_RESULT_ERROR);
            response.setResponseMsg("did not login");
        }

        return response.generateResponse();
    }

    /**
     * 修改账户密码
     *
     * @param passwordInfo 密码信息 传入加密后的密码
     * @param request      请求
     * @return 返回一个 Map 对象，其中键值为 result 代表修改密码操作的结果，
     * 值为 success 或 error；键值为 msg 代表需要返回给用户的信息
     */
    @RequestMapping(value = "passwordModify", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> passwordModify(@RequestBody Map<String, Object> passwordInfo,
                                       HttpServletRequest request) {

        //初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        String errorMsg = null;
        String result = Response.RESPONSE_RESULT_ERROR;

        // 获取用户 ID
        HttpSession session = request.getSession();
        Integer userID = (Integer) session.getAttribute("userID");

        try {
            // 更改密码
            accountService.passwordModify(userID, passwordInfo);

            result = Response.RESPONSE_RESULT_SUCCESS;
        } catch (UserAccountServiceException e) {
            errorMsg = e.getExceptionDesc();
        }
        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseMsg(errorMsg);
        return responseContent.generateResponse();
    }

    /**
     * 获取图形验证码 将返回一个包含4位字符（字母或数字）的图形验证码，并且将图形验证码的值设置到用户的 session 中
     *
     * @param time     时间戳
     * @param response 返回的 HttpServletResponse 响应
     */
    @RequestMapping(value = "checkCode/{time}", method = RequestMethod.GET)
    public void getCheckCode(@PathVariable("time") String time, HttpServletResponse response, HttpServletRequest request) {

//        System.out.println(time);

        BufferedImage checkCodeImage = null;
        String checkCodeString = null;

        // 获取图形验证码，依赖于util/CheckCodeGenerator
        Map<String, Object> checkCode = checkCodeGenerator.generlateCheckCode();

        if (checkCode != null) {
            checkCodeString = (String) checkCode.get("checkCodeString");
            checkCodeImage = (BufferedImage) checkCode.get("checkCodeImage");
        }

        if (checkCodeString != null && checkCodeImage != null) {
            //获取response.getOutputStream()
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                // 设置 Session
                HttpSession session = request.getSession();
                session.setAttribute("checkCode", checkCodeString);

                // 将验证码输出
                ImageIO.write(checkCodeImage, "png", outputStream);

                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setContentType("image/png");
            } catch (IOException e) {
                log.error("fail to get the ServletOutputStream");
            }
        }
    }
}
