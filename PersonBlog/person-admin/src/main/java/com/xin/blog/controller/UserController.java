package com.xin.blog.controller;

import com.xin.blog.dto.ChangeStatusDto;
import com.xin.blog.dto.UserDto;
import com.xin.blog.dto.UserPageDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.UserService;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, UserPageDto userPageDto){
        return userService.getDataList(pageNum,pageSize,userPageDto);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseResult getUserInfoById(@PathVariable("userId") Long userId){
        return userService.getUserInfoById(userId);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeStatusDto changeStatusDto){
        return userService.changeStatus(changeStatusDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") Long userId){
        userService.removeById(userId);
        return ResponseResult.okResult();
    }

}
