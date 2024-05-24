package com.manager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manager.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Long> selectPermissions(Long roleId);
}
