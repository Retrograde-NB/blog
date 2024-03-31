package com.xin.blog.controller;

import com.xin.blog.dto.InsertRoleDto;
import com.xin.blog.dto.RoleListDto;
import com.xin.blog.dto.UpdateRoleDto;
import com.xin.blog.dto.UpdateStatusDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.RoleService;
import com.xin.blog.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto){
        return roleService.getRoleList(pageNum,pageSize,roleListDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UpdateStatusDto updateStatusDto){
        return roleService.changeStatus(updateStatusDto);
    }

    @PostMapping
    public ResponseResult add(@RequestBody InsertRoleDto insertRoleDto){
        return roleService.add(insertRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleInfoById(@PathVariable("id") Long roleId){
        return roleService.getRoleInfoById(roleId);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @DeleteMapping("/{roleId}")
    public ResponseResult deleteRoleById(@PathVariable("roleId") Long roleId){
        return roleService.deleteRoleById(roleId);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }

}
