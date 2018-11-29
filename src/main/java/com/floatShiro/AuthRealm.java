package com.floatShiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.Configuration;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.floatShiro.model.Permission;
import com.floatShiro.model.Role;
import com.floatShiro.model.User;
import com.floatShiro.service.UserService;

/**
 * todo 认证方法
 * @author Float
 */
public class AuthRealm extends AuthorizingRealm{


    @Autowired
    private UserService userService;


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User)principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        Set<Role> roles = user.getRoles();
        List<String> permissionList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(roles)){
            for(Role role : roles){
                Set<Permission> permissions = role.getPermissions();
                if(!CollectionUtils.isEmpty(permissions)){
                    for(Permission permission : permissions){
                        permissionList.add(permission.getPname());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        return info;
    }

    //认证登陆
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /*强转为UsernamePasswordToken*/
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userName = usernamePasswordToken.getUsername();
        User user = userService.findByUserName(userName);
        return new SimpleAuthenticationInfo(user,user.getPassword(),this.getClass().getName());
    }
}
