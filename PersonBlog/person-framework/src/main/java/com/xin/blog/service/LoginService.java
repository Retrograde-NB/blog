package com.xin.blog.service;

import com.xin.blog.entity.User;
import com.xin.blog.res.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
