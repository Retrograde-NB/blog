package com.xin.blog.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 用户和角色关联表(UserRole)表实体类
 *
 * @author makejava
 * @since 2022-10-25 15:19:18
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_role")
public class UserRole  {
    private static final long serialVersionUID = 625337492348897098L;
    //用户ID@TableId
    private Long userId;
    //角色ID@TableId
    private Long roleId;
}
