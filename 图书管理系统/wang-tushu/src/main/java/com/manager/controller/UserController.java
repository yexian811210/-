package com.manager.controller;


import com.manager.common.annotation.LogToMysql;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.dto.RoleDto;
import com.manager.entity.User;
import com.manager.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @LogToMysql("用户登录")
    @PostMapping("/login")
    public AjaxResult<Object> login(@RequestBody User user, HttpServletRequest IpRequest) {
        return userService.login(user,IpRequest);
    }

    @GetMapping("/getUserInfo")
    public AjaxResult<User> info(@RequestParam("token") String token) {
        return userService.userInfo(token);
    }


    @GetMapping("/logout")
    public AjaxResult<String> logout() {
        return userService.logout();
    }


    @GetMapping("/pageSearch")
    public AjaxResult<PageResult<User>> pageSearch(PageRequest pageRequest) {
        PageResult<User> pageResult = userService.pageSearch(pageRequest);
        return AjaxResult.success(pageResult);
    }


    @GetMapping("/deleteUser")
    public AjaxResult<Object> deleteUser(@RequestParam("userId") Long userId, @RequestParam("userType") Integer userType) {
        return userService.deleteUser(userId, userType);
    }

    @PostMapping("/updateUser")
    public AjaxResult<Object> updateUser(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return userService.updateUser(multipartRequest);
    }

    @PostMapping("/addUser")
    public AjaxResult<Object> addUser(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        return userService.addUser(multipartRequest,request);
    }


    @PostMapping("/grantRole")
    public AjaxResult<Object> grantRole(@RequestBody RoleDto roleDto, @RequestHeader("token") String token) {
        return userService.grantRole(roleDto, token);
    }

    @PostMapping("/updatePassword")
    public AjaxResult<Object> updatePassword(@RequestParam("srcPassword") String srcPassword,
                                             @RequestParam("newPassword") String newPassword,
                                             @RequestHeader("token") String token) {
        return userService.updatePassword(srcPassword,newPassword, token);
    }


}

