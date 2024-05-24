package com.manager.mapper;

import com.manager.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RoleMapper extends BaseMapper<Role> {


    List<Long> selectRoleIdList(Long userId);

    List<Role> selectUserRole(Long userId);
}
