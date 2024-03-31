package com.xin.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xin.blog.dto.ChangeStatusDto;
import com.xin.blog.dto.UserDto;
import com.xin.blog.dto.UserPageDto;
import com.xin.blog.entity.Role;
import com.xin.blog.entity.User;
import com.xin.blog.entity.UserRole;
import com.xin.blog.exception.SystemException;
import com.xin.blog.mapper.UserMapper;
import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import com.xin.blog.service.RoleService;
import com.xin.blog.service.UserRoleService;
import com.xin.blog.service.UserService;
import com.xin.blog.utils.BeanCopyUtils;
import com.xin.blog.utils.SecurityUtils;
import com.xin.blog.vo.PageVo;
import com.xin.blog.vo.UserInfoVo;
import com.xin.blog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-10-18 10:19:34
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        this.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDataList(Integer pageNum, Integer pageSize, UserPageDto userPageDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userPageDto.getUserName()),User::getUserName,userPageDto.getUserName());
        queryWrapper.eq(StringUtils.hasText(userPageDto.getPhonenumber()),User::getPhonenumber,userPageDto.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(userPageDto.getStatus()),User::getStatus,userPageDto.getStatus());

        Page<User> page = new Page<>(pageNum,pageSize);
        this.page(page,queryWrapper);

         //TODO 修改
        List<User> records = page.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Long id = records.get(i).getId();
            List<UserRole> list = this.userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
            records.get(i).setUserRole(list);
        }


        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult addUser(UserDto addUserDto) {
        if (!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (nickNameExist(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 先保存 User
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        // 再保存  roleids
        List<UserRole> userRoleList = addUserDto.getRoleIds().stream()
                .map(roleid -> new UserRole(user.getId(), Long.parseLong(roleid)))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfoById(Long userId) {
        // 获取关联用户的roleids
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,userId);
        List<UserRole> list = userRoleService.list(queryWrapper);
        List<String> roleids = list.stream()
                .map(userRole -> userRole.getRoleId().toString())
                .collect(Collectors.toList());
        // 所有角色的列表
        List<Role> roleList = roleService.list();
        // 获取用户信息
        User user = this.getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(new UserVo(roleids,roleList,userInfoVo));
    }

    @Override
    public ResponseResult updateUser(UserDto userDto) {
        // 先删除再更新
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,userDto.getId());
        userRoleService.remove(queryWrapper);

        User user = this.getById(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setNickName(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setSex(userDto.getSex());
        user.setStatus(userDto.getStatus());
        this.updateById(user);

        List<UserRole> userRoleList = userDto.getRoleIds().stream()
                .map(roleid -> new UserRole(user.getId(), Long.parseLong(roleid)))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(ChangeStatusDto changeStatusDto) {
        User user = this.getById(changeStatusDto.getUserId());
        user.setStatus(changeStatusDto.getStatus());
        this.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public User getByIdSelectNickName(Long createBy) {
        return userMapper.getByIdSelectNickName(createBy);
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return this.count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return this.count(queryWrapper) > 0;
    }
    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return this.count(queryWrapper) > 0;
    }
}
