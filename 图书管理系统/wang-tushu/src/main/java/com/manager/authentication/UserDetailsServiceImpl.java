package com.manager.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manager.entity.User;
import com.manager.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        //如果没有查询到用户信息就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户或密码错误");
        }
        List<String> permissions = userMapper.selectPermissions(user.getUserId());
        return new LoginUserDetails(user,permissions);
    }
}
