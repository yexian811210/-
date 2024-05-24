package com.manager.controller;

import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.common.common.AjaxResult;
import com.manager.vo.RoleVo;
import com.manager.entity.Role;
import com.manager.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    @Resource
    private RoleService roleService;


    @GetMapping("/getRoleList")
    public AjaxResult<PageResult<Role>> getRoleList(PageRequest pageRequest) {
        return roleService.getRoleList(pageRequest);
    }


    @PostMapping("/updateRole")
    public AjaxResult<Object> updateRole(@RequestBody Role role, @RequestHeader("token") String token) {
        return roleService.updateRole(role, token);
    }


    @PostMapping("/addRole")
    public AjaxResult<Object> addRole(@RequestBody Role role, @RequestHeader("token") String token) {
        return roleService.addRole(role, token);
    }


    @GetMapping("/deleteRole")
    public AjaxResult<Object> deleteStudent(@RequestParam("roleId") Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @PostMapping("/grantPermissions")
    public AjaxResult<Object> grantPermissions(@RequestBody Role role, @RequestHeader("token") String token) {
        return roleService.grantPermissions(role,token);
    }

    @GetMapping("/getRoleAllList")
    public AjaxResult<List<RoleVo>> getRoleAllList() {
        return roleService.getRoleAllList();
    }

}

