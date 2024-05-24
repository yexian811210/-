package com.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.dto.RoleDto;
import com.manager.entity.User;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    AjaxResult<Object> login(User user, HttpServletRequest IpRequest);

    AjaxResult<String> logout();

    PageResult<User> pageSearch(PageRequest pageRequest);

    AjaxResult<User> userInfo(String token);

    AjaxResult<Object> deleteUser(Long userId, Integer userType);

    AjaxResult<Object> updateUser(MultipartHttpServletRequest request);

    AjaxResult<Object> addUser(MultipartHttpServletRequest request, HttpServletRequest IpRequest);

    AjaxResult<Object> grantRole(RoleDto roleDto, String token);

    AjaxResult<Object> updatePassword(String srcPassword, String newPassword, String token);
}
