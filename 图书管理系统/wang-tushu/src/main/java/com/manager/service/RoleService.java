package com.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Role;
import com.manager.vo.RoleVo;

import java.util.List;

public interface RoleService extends IService<Role> {

    AjaxResult<PageResult<Role>> getRoleList(PageRequest pageRequest);

    AjaxResult<Object> updateRole(Role role, String token);

    AjaxResult<Object> addRole(Role role, String token);

    AjaxResult<Object> deleteRole(Long roleId);

    AjaxResult<Object> grantPermissions(Role role, String token);

    AjaxResult<List<RoleVo>> getRoleAllList();

}
