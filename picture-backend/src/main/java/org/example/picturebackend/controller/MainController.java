package org.example.picturebackend.controller;

import org.example.picturebackend.common.BaseResponse;
import org.example.picturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Spring MVC 会自动处理路径拼接，会去除重复的斜杠
@RequestMapping("/")
public class MainController {
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
