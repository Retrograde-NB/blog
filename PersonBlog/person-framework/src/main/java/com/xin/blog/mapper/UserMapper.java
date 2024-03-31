package com.xin.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xin.blog.entity.User;
import org.apache.ibatis.annotations.Param;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-17 10:07:52
 */
public interface UserMapper extends BaseMapper<User> {

    User getByIdSelectNickName(@Param(value = "createBy") Long createBy);
}
