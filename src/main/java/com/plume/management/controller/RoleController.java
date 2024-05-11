package com.plume.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.common.Result;
import com.plume.management.pojo.Role;
import com.plume.management.pojo.User;
import com.plume.management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result page(@Parameter(description = "页数") @RequestParam Integer pageNum,
                            @Parameter(description = "页面大小") @RequestParam Integer pageSize,
                            @Parameter(description = "角色名称") @RequestParam(value = "name", required = false) String name){
        return Result.success(roleService.page(pageNum, pageSize,name));
    }

    @PostMapping("/save")
    public Result save(@RequestBody Role role) {
        // 判断id是否存在决定是保存还是更新
        if (role.getId() == null) {
            return Result.success(roleService.save(role));
        }
        return Result.success(roleService.updateById(role));
    }


    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return Result.success(roleService.delete(ids));
    }

    /**
     * 导出
     */
    // @GetMapping("/export")
    // public void export(HttpServletResponse response) throws Exception {
    //     userService.export(response);
    // }

    // @PostMapping("/import")
    // public Boolean imp(MultipartFile file) throws IOException {
    //     return roleService.imp(file);
    // }



    @GetMapping("/name/{name}")
    public Result findOne(@PathVariable String name){
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        return Result.success(roleService.getOne(queryWrapper));
    }

    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
        return Result.success(roleService.roleMenu(roleId,menuIds));
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }

    @GetMapping("/list")
    public Result list(){
        return Result.success(roleService.list());
    }

}
