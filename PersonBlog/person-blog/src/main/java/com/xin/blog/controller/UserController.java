package com.xin.blog.controller;

import com.xin.blog.annotatino.SystemLog;
import com.xin.blog.entity.User;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @SystemLog(businessName = "获取用户信息")
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @SystemLog(businessName = "更新用户信息")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @SystemLog(businessName = "用户注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
