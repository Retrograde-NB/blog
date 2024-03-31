package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.dto.MenuDto;
import com.xin.blog.dto.MenuInsertDto;
import com.xin.blog.dto.MenuUpdateDto;
import com.xin.blog.entity.Menu;
import com.xin.blog.entity.RoleMenu;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.MenuMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.MenuService;
import com.xin.blog.service.RoleMenuService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.utils.SecurityUtils;
import com.xin.blog.vo.MenuResVo;
import com.xin.blog.vo.MenuVo;
import com.xin.blog.vo.RoleMenuTreeselectVo;
import com.xin.blog.vo.TreeMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-20 08:45:41
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 管理员返回所有权限
        if(SystemConstants.ADMIN.equals(id)) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = this.list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        // 否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    @Override
    public ResponseResult getMenuList(MenuDto menuDto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuDto.getMenuName()),Menu::getMenuName,menuDto.getMenuName());
        queryWrapper.eq(Objects.nonNull(menuDto.getStatus()),Menu::getStatus,menuDto.getStatus());
        queryWrapper.orderByAsc(Menu::getParentId);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menuList = this.list(queryWrapper);
        List<MenuVo> menuVoList = BeanCopyUtils.copyBeanList(menuList,MenuVo.class);
        return ResponseResult.okResult(menuVoList);
    }

    @Override
    public ResponseResult insertMenu(MenuInsertDto menuInsertDto) {
        Menu menu = BeanCopyUtils.copyBean(menuInsertDto, Menu.class);
        this.save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuInfoById(Long id) {
        Menu menu = this.getById(id);
        if (Objects.isNull(menu)) {
            throw new SystemException(AppHttpCodeEnum.ID_IS_NOT);
        }
        MenuResVo menuResVo = BeanCopyUtils.copyBean(menu, MenuResVo.class);
        return ResponseResult.okResult(menuResVo);
    }

    @Override
    public ResponseResult updateMenu(MenuUpdateDto menuUpdateDto) {
        if (menuUpdateDto.getId().equals(menuUpdateDto.getParentId())) {
            throw new SystemException(AppHttpCodeEnum.PARENTID_IS_ID);
        }
        Menu menu = BeanCopyUtils.copyBean(menuUpdateDto, Menu.class);
        this.updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuById(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> menuList = this.list(queryWrapper);
        if (menuList.size() != 0){
            throw new SystemException(AppHttpCodeEnum.ID_IN_PARENTID);
        }
        this.removeById(menuId);
        return ResponseResult.okResult();
    }

    private List<TreeMenuVo> getTreeMenu(){
        // 先把父节点查出来
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,SystemConstants.PARENTID);
        queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menuList = this.list(queryWrapper);
        menuList = menuList.stream()
                .map(menu -> menu.setChildren(getTreeMenuChildren(menu.getId())))
                .collect(Collectors.toList());
        List<TreeMenuVo> treeMenuVos = menuList.stream()
                .map(menu -> {
                    TreeMenuVo treeMenuVo = new TreeMenuVo();
                    treeMenuVo.setId(menu.getId());
                    treeMenuVo.setLabel(menu.getMenuName());
                    treeMenuVo.setParentId(menu.getParentId());
                    treeMenuVo.setChildren(transitionVo(menu.getChildren()));
                    return treeMenuVo;
                })
                .collect(Collectors.toList());
        return treeMenuVos;
    }
    @Override
    public ResponseResult treeselect() {
        return ResponseResult.okResult(getTreeMenu());
    }

    private List<Menu> getTreeMenuChildren(Long menuId){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menuList = this.list(queryWrapper);
        if (menuList.size() != 0){
            menuList = menuList.stream()
                    .map(menu -> menu.setChildren(getTreeMenuChildren(menu.getId())))
                    .collect(Collectors.toList());
        }
        return menuList;
    }
    private List<TreeMenuVo> transitionVo(List<Menu> menuList){
        List<TreeMenuVo> menuVos = menuList.stream()
                .map(menu -> {
                    TreeMenuVo treeMenuVo = new TreeMenuVo();
                    treeMenuVo.setId(menu.getId());
                    treeMenuVo.setLabel(menu.getMenuName());
                    treeMenuVo.setParentId(menu.getParentId());
                    if (menu.getChildren().size() != 0) {
                        treeMenuVo.setChildren(transitionVo(menu.getChildren()));
                    } else {
                        List<TreeMenuVo> treeMenuVos = new ArrayList<>();
                        treeMenuVo.setChildren(treeMenuVos);
                    }
                    return treeMenuVo;
                })
                .collect(Collectors.toList());
        return menuVos;
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long roleId) {
        List<TreeMenuVo> treeMenuVos = getTreeMenu();
        List<Long> menuIdList = new ArrayList<>();
        if (roleId.equals(SystemConstants.ADMIN)){
            List<Menu> menuList = this.list();
            menuIdList = menuList.stream()
                    .map(menu -> menu.getId())
                    .collect(Collectors.toList());
        } else {
            LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RoleMenu::getRoleId,roleId);
            List<RoleMenu> roleMenuList = roleMenuService.list(queryWrapper);
            // 获取menuId
            menuIdList = roleMenuList.stream()
                    .map(roleMenu -> roleMenu.getMenuId())
                    .collect(Collectors.toList());
        }
        return ResponseResult.okResult(new RoleMenuTreeselectVo(treeMenuVos,menuIdList));
    }


}

