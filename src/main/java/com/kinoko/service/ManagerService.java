package com.kinoko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kinoko.entity.Manager;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService extends IService<Manager>{
    /**
     * 登录业务，密码用md5加密
     * @param name 管理员用户名
     * @param password 管理员密码
     * @throws AuthenticationException 如果登录失败抛出异常，在controller中捕获
     */
    void login(String name,String password);
}
