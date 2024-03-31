package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.InsertRoleDto;
import com.xin.blog.dto.RoleListDto;
import com.xin.blog.dto.UpdateRoleDto;
import com.xin.blog.dto.UpdateStatusDto;
import com.xin.blog.entity.Role;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.vo.PageVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-20 08:52:54
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult changeStatus(UpdateStatusDto updateStatusDto);

    ResponseResult add(InsertRoleDto insertRoleDto);

    ResponseResult getRoleInfoById(Long roleId);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRoleById(Long roleId);

    ResponseResult listAllRole();
}


