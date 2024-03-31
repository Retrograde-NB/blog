package com.xin.blog.controller;

import com.xin.blog.entity.LoginUser;
import com.xin.blog.entity.Menu;
import com.xin.blog.entity.User;
import com.xin.blog.exception.SystemException;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.LoginService;
import com.xin.blog.service.MenuService;
import com.xin.blog.service.RoleService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.utils.SecurityUtils;
import com.xin.blog.vo.AdminUserInfoVo;
import com.xin.blog.vo.RoutersVo;
import com.xin.blog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据用户id获取权限
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        // 封装数据放回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
