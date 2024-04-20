package com.plume.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.plume.management.common.Constants;
import com.plume.management.common.Result;
import com.plume.management.pojo.Dict;
import com.plume.management.pojo.Menu;
import com.plume.management.pojo.Role;
import com.plume.management.service.DictService;
import com.plume.management.service.MenuService;
import com.plume.management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private DictService dictService;


    @GetMapping("/findAll")
    @Operation(summary = "分页查询")
    public Result findAll(@Parameter(description = "角色名称") @RequestParam(value = "name", required = false) String name){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("id");
        List<Menu> list = menuService.list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).toList();
        // 找出一级菜单的子菜单
        for (Menu menu:parentNode) {
            // 筛选所有菜单中pid等于父菜单id的菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return Result.success(parentNode);
    }
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result page(@Parameter(description = "页数") @RequestParam Integer pageNum,
                            @Parameter(description = "页面大小") @RequestParam Integer pageSize,
                            @Parameter(description = "角色名称") @RequestParam(value = "name", required = false) String name){
        return Result.success(menuService.page(pageNum, pageSize,name));
    }

    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        // 判断id是否存在决定是保存还是更新
        if (menu.getId() == null) {
            return Result.success(menuService.save(menu));
        }
        return Result.success(menuService.updateById(menu));
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return Result.success(menuService.delete(ids));
    }

    @GetMapping("/name/{name}")
    public Result findOne(@PathVariable String name){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        return Result.success(menuService.getOne(queryWrapper));
    }

    @GetMapping("/icons")
    public Result getIcons(){
        return Result.success(dictService.list(new QueryWrapper<Dict>().eq("type", Constants.DICT_TYPE_ICON)));
    }

}
