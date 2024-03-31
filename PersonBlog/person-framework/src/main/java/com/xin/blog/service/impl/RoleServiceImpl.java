package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.InsertRoleDto;
import com.xin.blog.dto.RoleListDto;
import com.xin.blog.dto.UpdateRoleDto;
import com.xin.blog.dto.UpdateStatusDto;
import com.xin.blog.entity.Role;
import com.xin.blog.entity.RoleMenu;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.RoleMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.RoleMenuService;
import com.xin.blog.service.RoleService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.vo.PageVo;
import com.xin.blog.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-20 08:52:54
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // 管理员只需要返回admin
        if (SystemConstants.ADMIN.equals(id)){
            List<String> roleKey = new ArrayList<>();
            roleKey.add("admin");
            return roleKey;
        }
        // 否则需要查询所对应的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult<PageVo> getRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        // 条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleListDto.getRoleName()),Role::getRoleName,roleListDto.getRoleName());
        queryWrapper.eq(StringUtils.hasText(roleListDto.getStatus()),Role::getStatus,roleListDto.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort);
        // 分页
        Page<Role> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);
        // 封装
        List<RoleVo> roleVos = page.getRecords().stream()
                .map(role -> BeanCopyUtils.copyBean(role, RoleVo.class))
                .collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(UpdateStatusDto updateStatusDto) {
        Role role = this.getById(updateStatusDto.getRoleId());
        if (Objects.isNull(role)){
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        role.setStatus(updateStatusDto.getStatus());
        this.updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(InsertRoleDto insertRoleDto) {
        Role role = BeanCopyUtils.copyBean(insertRoleDto, Role.class);
        this.save(role);
        List<RoleMenu> menuList = insertRoleDto.getMenuIds().stream()
                .map(menu -> new RoleMenu(role.getId(), Long.parseLong(menu)))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(menuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleInfoById(Long roleId) {
        Role role = this.getById(roleId);
        if (Objects.isNull(role)) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        // 先删除再更新
        // 删除
        //roleMenuService.removeById(updateRoleDto.getId());
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, updateRoleDto.getId()));
        // 更新
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        // 获取roleMenuList并保存
        List<RoleMenu> roleMenuList = updateRoleDto.getMenuIds().stream()
                .map(menu -> new RoleMenu(updateRoleDto.getId(), Long.parseLong(menu)))
                .collect(Collectors.toList());
        this.updateById(role);
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long roleId) {
        if(Objects.isNull(this.getById(roleId))) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        this.removeById(roleId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus,SystemConstants.STATUS_NORMAL);
        List<Role> roleList = this.list(queryWrapper);

        List<RoleVo> roleVoList = BeanCopyUtils.copyBeanList(roleList, RoleVo.class);
        return ResponseResult.okResult(roleVoList);
    }
}
