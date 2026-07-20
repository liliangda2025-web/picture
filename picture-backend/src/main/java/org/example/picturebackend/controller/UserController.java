package org.example.picturebackend.controller;

import org.example.picturebackend.common.BaseResponse;
import org.example.picturebackend.common.ResultUtils;
import org.example.picturebackend.exception.ErrorCode;
import org.example.picturebackend.exception.ThrowUtil;
import org.example.picturebackend.model.dto.UserLoginRequest;
import org.example.picturebackend.model.dto.UserRegisterRequest;
import org.example.picturebackend.model.vo.LoginUserVO;
import org.example.picturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest request) {
        ThrowUtil.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        String userAccount = request.getUserAccount();
        String userPassword = request.getPassword();
        String checkPassword = request.getCheckPassword();

        long result = userService.Register(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtil.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getPassword();
        LoginUserVO result = userService.Login(userAccount, userPassword, request);
        return ResultUtils.success(result);

    }
}
