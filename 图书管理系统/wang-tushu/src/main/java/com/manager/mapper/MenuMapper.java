package com.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manager.common.MenuQuery;
import com.manager.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectMenuList(MenuQuery menuQuery);

    Long insertReturnMenuId(Menu menu);

}
