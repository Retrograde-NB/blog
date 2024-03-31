package com.xin.blog.controller;

import com.xin.blog.dto.MenuDto;
import com.xin.blog.dto.MenuInsertDto;
import com.xin.blog.dto.MenuUpdateDto;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.MenuService;
import com.xin.blog.vo.TreeMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(MenuDto menuDto){
        return menuService.getMenuList(menuDto);
    }

    @PostMapping
    public ResponseResult insertMenu(@RequestBody MenuInsertDto menuInsertDto){
        return menuService.insertMenu(menuInsertDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuInfoById(@PathVariable Long id){
        return menuService.getMenuInfoById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody MenuUpdateDto menuUpdateDto){
        return menuService.updateMenu(menuUpdateDto);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenuById(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenuById(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id") Long roleId){
        return menuService.roleMenuTreeselect(roleId);
    }
}
