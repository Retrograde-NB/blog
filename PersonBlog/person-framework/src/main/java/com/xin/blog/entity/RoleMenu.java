package com.xin.blog.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2022-10-22 09:33:51
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu  {
    private static final long serialVersionUID = 625337492348897098L;
    //角色ID@TableId

    private Long roleId;
    //菜单ID@TableId
    private Long menuId;




}
