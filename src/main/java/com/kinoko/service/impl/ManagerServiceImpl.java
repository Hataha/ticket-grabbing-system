package com.kinoko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.entity.Manager;
import com.kinoko.dao.ManagerMapper;
import com.kinoko.service.ManagerService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper,Manager> implements ManagerService {

    @Override
    public void login(String name, String password) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        subject.login(token);
    }
}
