package com.manager.service;

import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.common.common.AjaxResult;
import com.manager.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface MenuService extends IService<Menu> {

    AjaxResult<List<Menu>> getMenuList(String token);

    AjaxResult<PageResult<Menu>> getMenuPageList(String token, PageRequest pageRequest);

    AjaxResult<Object> updateMenu(Menu menu, String token);

    AjaxResult<Object> addMenu(Menu menu, String token);

    AjaxResult<List<Menu>> getMenuDirList(String token);

    AjaxResult<Object> deleteMenu(Long menuId);

    AjaxResult<List<Menu>> getMenuAllList();

}
