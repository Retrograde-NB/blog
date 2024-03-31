package com.xin.blog.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xin.blog.constans.SystemConstants;
import com.xin.blog.entity.LoginUser;
import com.xin.blog.entity.User;
import com.xin.blog.mapper.MenuMapper;
import com.xin.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        // 判断是否查询到用户
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //User(id=14787164048667, userName=leixin, nickName=lx, password=$2a$10$sidk5/ViHfwCLbBQMFOnguCQl4aOQDmE9UQI7mYN.d/W7zFCJ2HdG, type=1, status=0, email=2352017802@qq.com, phonenumber=18573757215, sex=0, avatar=null, createBy=null, createTime=null, updateBy=null, updateTime=null, delFlag=0, userRole=null)
        // 返回用户信息
        // 查询权限并封装 (后台用户才需要封装)
        if (user.getType().equals(SystemConstants.ADMIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            System.out.println("================================================================================");
            System.out.println(user);
            System.out.println(list);
            return new LoginUser(user,list);
        }
        return new LoginUser(user,null);
    }
}
