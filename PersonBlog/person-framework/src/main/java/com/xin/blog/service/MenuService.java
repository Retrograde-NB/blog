package com.xin.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xin.blog.dto.MenuDto;
import com.xin.blog.dto.MenuInsertDto;
import com.xin.blog.dto.MenuUpdateDto;
import com.xin.blog.entity.Menu;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.vo.TreeMenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-10-20 08:45:41
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult getMenuList(MenuDto menuDto);

    ResponseResult insertMenu(MenuInsertDto menuInsertDto);

    ResponseResult getMenuInfoById(Long id);

    ResponseResult updateMenu(MenuUpdateDto menuUpdateDto);

    ResponseResult deleteMenuById(Long menuId);

    ResponseResult treeselect();

    ResponseResult roleMenuTreeselect(Long roleId);
}


