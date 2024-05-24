package com.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.manager.common.MenuQuery;
import com.manager.common.annotation.CustomTransaction;
import com.manager.common.common.AjaxResult;
import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.entity.Menu;
import com.manager.entity.RoleMenu;
import com.manager.mapper.MenuMapper;
import com.manager.mapper.RoleMapper;
import com.manager.mapper.RoleMenuMapper;
import com.manager.service.MenuService;
import com.manager.utils.JwtUtils;
import com.manager.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RoleMapper roleMapper;


    @Override
    public AjaxResult<List<Menu>> getMenuList(String token) {
        String userId = JwtUtils.parseToken(token);
        MenuQuery menuQuery = new MenuQuery();
        menuQuery.setUserId(Long.valueOf(userId));
        if (!Objects.equals(userId, "1")) {
            menuQuery.setStatus(0);
        }
        List<Menu> menuList = this.baseMapper.selectMenuList(menuQuery);
        if (!CollectionUtils.isEmpty(menuList)) {
            menuList = menuList.stream().sorted(Comparator.comparingInt(Menu::getOrderBy)).collect(Collectors.toList());
            List<Menu> topLeveMenuList = menuList.stream().filter(menu -> Objects.equals(menu.getParentId(), 0)).collect(Collectors.toList());
            for (Menu topLeveMenu : topLeveMenuList) {
                topLeveMenu.setChildren(getChildrenMenu(menuList, topLeveMenu.getMenuId()));
            }
            return AjaxResult.success(topLeveMenuList);
        }
        return AjaxResult.success(new ArrayList<>());

    }

    @Override
    public AjaxResult<PageResult<Menu>> getMenuPageList(String token, PageRequest pageRequest) {
        String userId = JwtUtils.parseToken(token);
        List<Menu> menuList;
        List<Menu> topLeveMenuList;
        MenuQuery menuQuery = new MenuQuery();
        menuQuery.setUserId(Long.valueOf(userId));
        if (StringUtils.hasText(pageRequest.getKey())) {
            menuList = this.baseMapper.selectMenuList(menuQuery).stream()
                    .sorted(Comparator.comparingInt(Menu::getOrderBy))
                    .collect(Collectors.toList());

            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
            menuQuery.setParentId(0);
            menuQuery.setLabel(pageRequest.getKey());
            topLeveMenuList = this.baseMapper.selectMenuList(menuQuery);
        } else {
            menuList = this.baseMapper.selectMenuList(menuQuery).stream().sorted(Comparator.comparingInt(Menu::getOrderBy)).collect(Collectors.toList());
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
            menuQuery.setParentId(0);
            topLeveMenuList = this.baseMapper.selectMenuList(menuQuery);
        }
        topLeveMenuList = topLeveMenuList.stream().sorted(Comparator.comparingInt(Menu::getOrderBy)).collect(Collectors.toList());
        PageInfo<Menu> pageInfo = new PageInfo<>(topLeveMenuList);
        PageResult<Menu> pageResult = PageUtils.getPageResult(pageInfo);
        for (Menu topLeveMenu : pageResult.getDataList()) {
            topLeveMenu.setChildren(getChildrenMenu(menuList, topLeveMenu.getMenuId()));
        }

        return AjaxResult.success(pageResult);
    }

    @Override
    public AjaxResult<Object> updateMenu(Menu menu, String token) {
        try {
            if (Objects.nonNull(menu)) {
                String userId = JwtUtils.parseToken(token);
                menu.setUpdateTime(new Date());
                menu.setUpdateBy(Long.valueOf(userId));
                this.baseMapper.updateById(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<Object> addMenu(Menu menu, String token) {
        try {
            if (StringUtils.hasText(token)) {
                String userId = JwtUtils.parseToken(token);
                if (Objects.nonNull(menu)) {
                    menu.setCreateBy(Long.valueOf(userId));
                    menu.setUpdateBy(Long.valueOf(userId));
                    Date date = new Date();
                    menu.setUpdateTime(date);
                    menu.setCreateTime(date);
                    List<Long> roleIdList = roleMapper.selectRoleIdList(Long.valueOf(userId));
                    this.baseMapper.insertReturnMenuId(menu);
                    for (Long roleId : roleIdList) {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menu.getMenuId());
                        roleMenuMapper.insert(roleMenu);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return AjaxResult.fail();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult<List<Menu>> getMenuDirList(String token) {
        String userId = JwtUtils.parseToken(token);
        MenuQuery menuQuery = new MenuQuery();
        menuQuery.setUserId(Long.valueOf(userId));
        List<Menu> menuList = this.baseMapper.selectMenuList(menuQuery);
        if (!CollectionUtils.isEmpty(menuList)) {
            List<Menu> menuDirList = menuList.stream().filter(menu -> !StringUtils.hasText(menu.getComponent())).collect(Collectors.toList());
            List<Menu> topLeveMenuDirList = menuDirList.stream().filter(menu -> Objects.equals(menu.getParentId(), 0)).collect(Collectors.toList());
            for (Menu topLeveMenuDir : topLeveMenuDirList) {
                topLeveMenuDir.setChildren(getChildrenMenu(menuDirList, topLeveMenuDir.getMenuId()));
            }
            return AjaxResult.success(topLeveMenuDirList);
        }
        return AjaxResult.success(new ArrayList<>());
    }

    @Override
    @CustomTransaction("删除菜单失败")
    public AjaxResult<Object> deleteMenu(Long menuId) {
        if (Objects.nonNull(menuId)) {
            List<Menu> menuList = new ArrayList<>();
            Menu menu = this.getById(menuId);
            menuList.add(menu);
            selectChildren(menuList,menuId);
            List<Long> menuIdList = menuList.stream().map(Menu::getMenuId).collect(Collectors.toList());
            LambdaQueryWrapper<Menu> menuWrapper = new LambdaQueryWrapper<>();
            menuWrapper.in(Menu::getMenuId,menuIdList);
            this.baseMapper.delete(menuWrapper);

            LambdaQueryWrapper<RoleMenu> roleMenuWrapper = new LambdaQueryWrapper<>();
            roleMenuWrapper.in(RoleMenu::getMenuId,menuIdList);
            roleMenuMapper.delete(roleMenuWrapper);
        }
        return AjaxResult.success();
    }

    public void selectChildren(List<Menu> childrenMenuList, Long menuId) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery(Menu.class);
        wrapper.eq(Menu::getParentId, menuId);
        List<Menu> menuList = this.baseMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(menuList)) {
            for (Menu menu : menuList) {
                selectChildren(childrenMenuList, menu.getMenuId());
            }
            childrenMenuList.addAll(menuList);
        }
    }

    @Override
    public AjaxResult<List<Menu>> getMenuAllList() {
        List<Menu> menuList = this.baseMapper.selectList(null);
        if (!CollectionUtils.isEmpty(menuList)) {
            menuList = menuList.stream().sorted(Comparator.comparingInt(Menu::getOrderBy)).collect(Collectors.toList());
            List<Menu> topLeveMenuList = menuList.stream().filter(menu -> Objects.equals(menu.getParentId(), 0)).collect(Collectors.toList());
            for (Menu topLeveMenu : topLeveMenuList) {
                topLeveMenu.setChildren(getChildrenMenu(menuList, topLeveMenu.getMenuId()));
            }
            return AjaxResult.success(topLeveMenuList);
        }
        return AjaxResult.success(new ArrayList<>());
    }


    private List<Menu> getChildrenMenu(List<Menu> menuList, long menuId) {
        System.out.println(menuId);
        List<Menu> nextLevelMenuList = menuList.stream().filter(menu -> menu.getParentId() == menuId).collect(Collectors.toList());
        for (Menu nextLevelMenu : nextLevelMenuList) {
            nextLevelMenu.setChildren(getChildrenMenu(menuList, nextLevelMenu.getMenuId()));
        }
        return nextLevelMenuList;
    }
}




