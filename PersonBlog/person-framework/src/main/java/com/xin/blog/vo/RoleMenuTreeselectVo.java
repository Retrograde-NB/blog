package com.xin.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeselectVo {
    private List<TreeMenuVo> menus;
    private List<Long> checkedKeys;
}
