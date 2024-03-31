package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.ChangeStatusDto;
import com.xin.blog.dto.UserDto;
import com.xin.blog.dto.UserPageDto;
import com.xin.blog.entity.User;
import com.xin.blog.res.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-10-18 10:19:34
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getDataList(Integer pageNum, Integer pageSize, UserPageDto userPageDto);

    ResponseResult addUser(UserDto addUserDto);

    ResponseResult getUserInfoById(Long userId);

    ResponseResult updateUser(UserDto userDto);

    ResponseResult changeStatus(ChangeStatusDto changeStatusDto);

    User getByIdSelectNickName(Long createBy);
}


