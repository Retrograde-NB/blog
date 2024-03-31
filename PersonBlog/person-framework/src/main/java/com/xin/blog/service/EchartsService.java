package com.xin.blog.service;

import com.xin.blog.res.ResponseResult;

public interface EchartsService {
    ResponseResult getTypeList();

    ResponseResult getBlogType();

    ResponseResult getCommentList(Integer pageNum, Integer pageSize);

    ResponseResult getTendencyData();
}
