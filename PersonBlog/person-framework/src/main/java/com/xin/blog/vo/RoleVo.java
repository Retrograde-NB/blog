package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private Long id;
    private String roleKey;
    private String roleName;
    private Integer roleSort;
    private String status;
    private String remark;
}
