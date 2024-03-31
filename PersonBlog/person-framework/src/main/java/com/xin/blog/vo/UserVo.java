package com.xin.blog.vo;

import com.xin.blog.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private List<String> roleIds;
    private List<Role> roles;
    private UserInfoVo user;
}
