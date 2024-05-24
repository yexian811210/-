package com.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Role;
import com.manager.entity.RoleMenu;
import com.manager.entity.UserRole;
import com.manager.mapper.RoleMapper;
import com.manager.mapper.RoleMenuMapper;
import com.manager.service.RoleMenuService;
import com.manager.service.RoleService;
import com.manager.service.UserRoleService;
import com.manager.service.UserService;
import com.manager.utils.AssertUtils;
import com.manager.utils.JwtUtils;
import com.manager.utils.PageUtils;
import com.manager.vo.RoleVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserService userService;

    @Override
    public AjaxResult<PageResult<Role>> getRoleList(PageRequest pageRequest) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        String key = pageRequest.getKey();
        if (AssertUtils.isNotEmpty(key)) {
            wrapper.like(Role::getName, key);
        }
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<Role> roleList = this.baseMapper.selectList(wrapper);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        PageResult<Role> pageResult = PageUtils.getPageResult(pageInfo);
        for (Role role : pageInfo.getList()) {
            List<Long> permissions = roleMenuMapper.selectPermissions(role.getRoleId());
            role.setPermissionList(permissions);
        }
        return AjaxResult.success(pageResult);
    }

    @Override
    public AjaxResult<Object> updateRole(Role role, String token) {
        if (Objects.nonNull(role)) {
            if (Objects.equals(role.getRoleKey(),"system:role:admin")){
                return AjaxResult.fail("无法更改超级管理员角色");
            }
            String userId = JwtUtils.parseToken(token);
            role.setUpdateBy(Long.valueOf(userId));
            role.setUpdateTime(new Date());
            this.updateById(role);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<Object> addRole(Role role, String token) {
        if (Objects.nonNull(role)) {
            String userId = JwtUtils.parseToken(token);
            role.setUpdateBy(Long.valueOf(userId));
            role.setCreateBy(Long.valueOf(userId));
            Date date = new Date();
            role.setUpdateTime(date);
            role.setCreateTime(date);
            this.save(role);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<Object> deleteRole(Long roleId) {
        if (Objects.nonNull(roleId)) {
            Role role = this.getById(roleId);
            if (Objects.equals(role.getRoleKey(),"system:role:admin")){
                return AjaxResult.fail("无法删除超级管理员角色");
            }
            this.removeById(roleId);
            LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
            roleMenuWrapper.eq(RoleMenu::getRoleId, roleId);
            roleMenuService.remove(roleMenuWrapper);
            LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.eq(UserRole::getRoleId, roleId);
            userRoleService.remove(userRoleWrapper);
            return AjaxResult.success();
        }
        return AjaxResult.fail();
    }

    @Override
    public AjaxResult<Object> grantPermissions(Role role, String token) {
        if (Objects.equals(role.getRoleKey(),"system:role:admin")){
            return AjaxResult.fail("无法更改管理员角色权限");
        }
        LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
        roleMenuWrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuService.remove(roleMenuWrapper);
        List<Long> permissionList = role.getPermissionList();
        if (!CollectionUtils.isEmpty(permissionList)) {
            List<RoleMenu> roleMenuList = permissionList.stream().map(p -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getRoleId());
                roleMenu.setMenuId(p);
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<List<RoleVo>> getRoleAllList() {
        List<Role> roleList = this.list();
        if (!CollectionUtils.isEmpty(roleList)) {
            List<RoleVo> roleVoList = roleList.stream().map(role -> {
                RoleVo roleVo = new RoleVo();
                roleVo.setRoleName(role.getName());
                roleVo.setRoleId(role.getRoleId());
                return roleVo;
            }).collect(Collectors.toList());
            return AjaxResult.success(roleVoList);
        }
        return AjaxResult.success(Collections.emptyList());
    }
}




