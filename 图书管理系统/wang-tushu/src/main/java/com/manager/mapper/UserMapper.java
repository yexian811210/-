package com.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manager.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //查询用户权限
    List<String> selectPermissions(Long userId);


    int insertReturnUserId(User user);

    //更新用户ip
    int updateUserByIp(User user);

    //更新时间
    int updateUserByTime(User user);
}
