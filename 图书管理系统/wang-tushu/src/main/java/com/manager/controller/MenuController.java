package com.manager.controller;

import com.manager.common.common.PageRequest;
import com.manager.common.common.PageResult;
import com.manager.common.common.AjaxResult;
import com.manager.entity.Menu;
import com.manager.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;


    @GetMapping("/getMenuList")
    public AjaxResult<List<Menu>> getMenuList(@RequestHeader("token") String token) {
        return menuService.getMenuList(token);
    }

    @GetMapping("/getMenuAllList")
    public AjaxResult<List<Menu>> getMenuAllList() {
        return menuService.getMenuAllList();
    }

    @GetMapping("/getMenuPageList")
    public AjaxResult<PageResult<Menu>> getMenuPageList(@RequestHeader("token") String token, PageRequest pageRequest) {
        return menuService.getMenuPageList(token, pageRequest);
    }


    @PostMapping("/updateMenu")
    public AjaxResult<Object> updateMenu(@RequestBody Menu menu, @RequestHeader("token") String token) {
        return menuService.updateMenu(menu,token);
    }


    @PostMapping("/addMenu")
    public AjaxResult<Object> addMenu(@RequestBody Menu menu, @RequestHeader("token") String token) {
        return menuService.addMenu(menu,token);
    }

    @GetMapping("/getMenuDirList")
    public AjaxResult<List<Menu>> getMenuDirList(@RequestHeader("token") String token) {
        return menuService.getMenuDirList(token);
    }


    @GetMapping("/deleteMenu")
    public AjaxResult<Object> deleteMenu(@RequestParam("menuId") Long menuId) {
        return menuService.deleteMenu(menuId);
    }
}
