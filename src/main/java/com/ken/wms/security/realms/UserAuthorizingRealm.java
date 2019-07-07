package com.ken.wms.security.realms;

import com.ken.wms.common.service.Interface.RepositoryAdminManageService;
import com.ken.wms.common.service.Interface.SystemLogService;
import com.ken.wms.domain.RepositoryAdmin;
import com.ken.wms.domain.UserInfoDTO;
import com.ken.wms.exception.RepositoryAdminManageServiceException;
import com.ken.wms.exception.UserInfoServiceException;
import com.ken.wms.security.service.Interface.UserInfoService;
import com.ken.wms.security.util.EncryptingModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户的认证与授权
 *
 * @author ken
 * @since 2017/2/26.
 */
public class UserAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private EncryptingModel encryptingModel;
    @Autowired
    private RepositoryAdminManageService repositoryAdminManageService;
    @Autowired
    private SystemLogService systemLogService;

    /**
     * 对用户进行角色授权
     *
     * @param principalCollection 用户信息
     * @return 返回用户授权信息
     */
    @SuppressWarnings("unchecked")
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 创建存放用户角色的 Set
        Set<String> roles = new HashSet<>();

        //获取用户角色
        Object principal = principalCollection.getPrimaryPrincipal();
        if (principal instanceof String) {
            String userID = (String) principal;
            if (StringUtils.isNumeric(userID)) {
                try {
                    UserInfoDTO userInfo = userInfoService.getUserInfo(Integer.valueOf(userID));
                    if (userInfo != null) {
                        // 设置用户角色
                        roles.addAll(userInfo.getRole());
                    }
                } catch (UserInfoServiceException e) {
                    // do logger
                }
            }
        }

        return new SimpleAuthorizationInfo(roles);
    }

    /**
     * 对用户进行认证
     *
     * @param authenticationToken 用户凭证
     * @return 返回用户的认证信息
     * @throws AuthenticationException 用户认证异常信息
     * Realm的认证方法，自动将token传入，比较token与数据库的数据是否匹配
     * 验证逻辑是先根据用户名查询用户，
     * 如果查询到的话再将查询到的用户名和密码放到SimpleAuthenticationInfo对象中，
     * Shiro会自动根据用户输入的密码和查询到的密码进行匹配，如果匹配不上就会抛出异常，
     * 匹配上之后就会执行doGetAuthorizationInfo()进行相应的权限验证。
     */
    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws
            AuthenticationException {

        String realmName = getName();
        String credentials = "";

        // 获取用户名对应的用户账户信息
        try {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
            String principal = usernamePasswordToken.getUsername();

            if (!StringUtils.isNumeric(principal))
                throw new AuthenticationException();
            Integer userID = Integer.valueOf(principal);
            //依赖于/security.service.Interface.UserInfoService,UserInfoDTO中包含用户ID,用户名，密码，角色
            //wms_user表
            UserInfoDTO userInfoDTO = userInfoService.getUserInfo(userID);

            if (userInfoDTO != null) {
                Subject currentSubject = SecurityUtils.getSubject();
                Session session = currentSubject.getSession();

                // 设置部分用户信息到 Session
                session.setAttribute("userID", userID);
                session.setAttribute("userName", userInfoDTO.getUserName());
                //获取该用户的所属仓库
                List<RepositoryAdmin> repositoryAdmin = (List<RepositoryAdmin>) repositoryAdminManageService.selectByID(userInfoDTO.getUserID()).get("data");
                session.setAttribute("repositoryBelong", (repositoryAdmin.isEmpty()) ? "none" : repositoryAdmin.get(0).getRepositoryBelongID());


                // 结合验证码对密码进行处理
                String checkCode = (String) session.getAttribute("checkCode");
                String password = userInfoDTO.getPassword();
                if (checkCode != null && password != null) {
                    checkCode = checkCode.toUpperCase();
                    credentials = encryptingModel.MD5(password + checkCode);
                }
            }
            //比对账号密码
            //principal前端传来userid
            //credentials为数据库的密码，加chexkcode的MD5加密
            //realmName为com.ken.wms.security.realms.UserAuthorizingRealm_0
            return new SimpleAuthenticationInfo(principal, credentials, realmName);

        } catch (UserInfoServiceException | RepositoryAdminManageServiceException | NoSuchAlgorithmException e) {
            throw new AuthenticationException();
        }
    }
}
