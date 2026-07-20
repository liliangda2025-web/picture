package org.example.picturebackend.service;

import org.example.picturebackend.model.dto.UserRegisterRequest;
import org.example.picturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-07-20 18:13:58
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param password 用户密码
     * @param checkPassword 密码确认
     * @return
     */
    long Register(String userAccount,String password,String checkPassword);

    /**
     * 密码加密
     * @param password
     */
    String getEncryptPassword(String password);

}
