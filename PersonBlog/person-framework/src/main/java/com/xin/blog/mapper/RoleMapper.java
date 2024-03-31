package com.xin.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xin.blog.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-20 08:52:54
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
