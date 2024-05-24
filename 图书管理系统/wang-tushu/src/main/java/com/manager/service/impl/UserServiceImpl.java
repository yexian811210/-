package com.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.manager.authentication.LoginUserDetails;
import com.manager.common.annotation.CustomTransaction;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.dto.RoleDto;
import com.manager.entity.Role;
import com.manager.entity.User;
import com.manager.entity.UserRole;
import com.manager.mapper.RoleMapper;
import com.manager.mapper.UserMapper;
import com.manager.mapper.UserRoleMapper;
import com.manager.service.UserService;
import com.manager.utils.*;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AliOssUtil aliOssUtil;

    @Resource
    private UserMapper userMapper;



    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public AjaxResult<Object> login(User user, HttpServletRequest IpRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //进行用户验证，调用UserDetailsServiceImpl中的loadUserByUsername方法验证用户信息
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // authenticate 不为 null 认证通过，为 null 认证没通过
        if (Objects.isNull(authenticate)) {
            return AjaxResult.fail("用户名或密码错误");
        }
        //验证通过，从authenticate中获取用户信息
        LoginUserDetails loginUserDetails = (LoginUserDetails) authenticate.getPrincipal();

        //获取用户id
        String userId = loginUserDetails.getUser().getUserId().toString();

        User userEntity = this.baseMapper.selectById(userId);
        userEntity.setIp(IpUtil.getIpAddress(IpRequest));
        userEntity.setUpdateTime(new Date());
        userMapper.updateUserByIp(userEntity);
        userMapper.updateUserByTime(userEntity);
        //判断用户是否停用
        if (Objects.equals(userEntity.getStatus(),"1")){
            return AjaxResult.fail("用户状态异常，请联系管理员!!!");
        }
        String jwt = JwtUtils.generateToken(userId);
        Map<String, String> data = new HashMap<>();
        data.put("token", jwt);
        redisTemplate.opsForValue().set("user:" + userId, loginUserDetails);
        return AjaxResult.success(data);
    }


    /**
     * 退出登录
     */
    @Override
    public AjaxResult<String> logout() {
        //获取 SecurityContextHolder 中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        String userId = loginUserDetails.getUser().getUserId().toString();
        //删除redis用户缓存
        redisTemplate.delete("user:" + userId);
        return AjaxResult.success("注销成功");
    }

    //用户分页查询
    @Override
    public PageResult<User> pageSearch(PageRequest pageRequest) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String key = pageRequest.getKey();
        if (AssertUtils.isNotEmpty(key)) {
            wrapper.like(User::getNickName, key);
        }
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<User> userList = this.baseMapper.selectList(wrapper);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        for (User user : pageInfo.getList()) {
            List<Role> roleList = roleMapper.selectUserRole(user.getUserId());
            if (!CollectionUtils.isEmpty(roleList)){
                user.setRoleIdList(roleList.stream().map(Role::getRoleId).collect(Collectors.toList()));
                user.setRoleNameList(roleList.stream().map(Role::getName).collect(Collectors.toList()));
            }
        }
        return PageUtils.getPageResult(pageInfo);
    }

    @Override
    public AjaxResult<User> userInfo(String token) {
        String userId = JwtUtils.parseToken(token);
        User user = baseMapper.selectById(userId);
        List<Role> roleList = roleMapper.selectUserRole(Long.valueOf(userId));
        user.setRoleNameList(roleList.stream().map(Role::getName).collect(Collectors.toList()));
        return AjaxResult.success(user);
    }

    @CustomTransaction
    @Override
    public AjaxResult<Object> deleteUser(Long userId, Integer userType) {
        User user = this.getById(userId);
        if (Objects.equals(user.getUserName(),"admin")){
            return AjaxResult.fail("无法删除超级管理员");
        }
        this.baseMapper.deleteById(userId);
        //删除阿里云oss存储图片
        aliOssUtil.delete(user.getAvatar().replaceFirst("^.*//.*aliyuncs.com/", "").replaceFirst("\\?.*",""));
        return AjaxResult.success();
    }

    @SneakyThrows
    @Override
    public AjaxResult<Object> updateUser(MultipartHttpServletRequest request) {
        String userId = JwtUtils.parseToken(request.getHeader("token"));
        User user = JSON.parseObject(request.getParameter("user"), User.class);
        if (Objects.equals(user.getUserName(),"admin")){
            return AjaxResult.fail("无法更改超级管理员");
        }
        MultipartFile file = request.getFile("file");
        if (Objects.nonNull(file)){
            aliOssUtil.delete(user.getAvatar().replaceFirst("^.*//.*aliyuncs.com/", "").replaceFirst("\\?.*",""));
            String url = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());user.setAvatar(url);
            user.setAvatar(url);
        }
        if (StringUtils.hasText(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUpdateBy(Long.valueOf(userId));
        user.setUpdateTime(new Date());
        this.baseMapper.updateById(user);
        return AjaxResult.success();
    }


    /**
     * 新增用户
     * 上传文件至阿里云，并将oss url设置为头像地址
     * 加密密码
     */
    @SneakyThrows
    @Override
    @CustomTransaction("用户新增失败")
    public AjaxResult<Object> addUser(MultipartHttpServletRequest request, HttpServletRequest IpRequest) {
        String userId = JwtUtils.parseToken(request.getHeader("token"));
        User user = JSON.parseObject(request.getParameter("user"), User.class);
        MultipartFile file = request.getFile("file");
        if(Objects.nonNull(file)){
            String url =  aliOssUtil.upload(Objects.requireNonNull(file).getBytes(), file.getOriginalFilename());
            user.setAvatar(url);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateBy(Long.valueOf(userId));
        user.setUpdateBy(Long.valueOf(userId));
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setIp(IpUtil.getIpAddress(IpRequest));
        this.baseMapper.insertReturnUserId(user);
        return AjaxResult.success();
    }
    @Override
    /**
    用户管理授权
    */
    public AjaxResult<Object> grantRole(RoleDto roleDto, String token) {
        User user = this.getById(roleDto.getUserId());
        if (Objects.equals(user.getUserName(),"admin")){
            return AjaxResult.fail("无法更改超级管理员角色");
        }
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,roleDto.getUserId());
        this.userRoleMapper.delete(wrapper);
        List<Long> roleIdList = roleDto.getRoleIdList();
        for (Long roleId : roleIdList) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(roleDto.getUserId());
            this.userRoleMapper.insert(userRole);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<Object> updatePassword(String srcPassword, String newPassword, String token) {
        String userId = JwtUtils.parseToken(token);
        User user = this.getById(userId);
        if (passwordEncoder.matches(srcPassword,user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            this.updateById(user);
            return AjaxResult.success("修改成功");
        }
        return AjaxResult.fail("原密码错误，请重新输入");
    }

}
