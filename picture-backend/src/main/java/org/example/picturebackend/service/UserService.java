package org.example.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.picturebackend.model.dto.user.UserQueryRequest;
import org.example.picturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.picturebackend.model.vo.LoginUserVO;
import org.example.picturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-07-20 18:13:58
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param password      用户密码
     * @param checkPassword 密码确认
     * @return
     */
    long Register(String userAccount, String password, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param password
     * @return
     */
    LoginUserVO Login(String userAccount, String password, HttpServletRequest request);

    /**
     * 密码加密
     *
     * @param password
     */
    String getEncryptPassword(String password);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏后的当前用户信息
     *
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏后的查询的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏后的查询的用户信息列表
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 将查询请求转换为条件构造器用于查询
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 判断当前用户是否是管理员
     *
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

}
