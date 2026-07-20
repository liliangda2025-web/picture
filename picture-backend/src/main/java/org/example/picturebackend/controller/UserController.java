package org.example.picturebackend.controller;

import org.example.picturebackend.common.BaseResponse;
import org.example.picturebackend.common.ResultUtils;
import org.example.picturebackend.exception.ErrorCode;
import org.example.picturebackend.exception.ThrowUtil;
import org.example.picturebackend.model.dto.UserRegisterRequest;
import org.example.picturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
