package com.kinoko.config.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kinoko.entity.Manager;
import com.kinoko.service.ManagerService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class ManagerRealm extends AuthorizingRealm {

    @Autowired
    private ManagerService managerService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        Manager manager = managerService.getOne(new QueryWrapper<Manager>()
                .eq("name",token.getUsername()));

        if(manager == null){
            return null;
        }
        else{
            return new SimpleAuthenticationInfo(manager,manager.getPassword(),getName());
        }
    }
}
