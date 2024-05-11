package com.plume.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.common.Result;
import com.plume.management.pojo.User;
import com.plume.management.pojo.dto.UserDTO;
import com.plume.management.service.UserService;
import com.plume.management.utils.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户操作", description = "用户的一系列操作")
public class UserController {
    @Autowired
    private UserService userService;


    /**
     * 分页查询
     *
     * @return
     */
    // @GetMapping("/page")
    // @Operation(summary = "分页查询")
    // public Map<String, Object> page(@Parameter(description = "页数")@RequestParam Integer pageNum,
    //                                 @Parameter(description = "页面大小")@RequestParam Integer pageSize,
    //                                 @Parameter(description = "用户名") @RequestParam(value = "userName",required = false) String userName,
    //                                 @Parameter(description = "邮箱")@RequestParam(value = "email",required = false) String email,
    //                                 @Parameter(description = "地址")@RequestParam(value = "address",required = false) String address){
    //     pageNum = (pageNum - 1) * pageSize;
    //     List<User> userList = userService.page(pageNum, pageSize,userName,email,address);
    //     Integer total = userService.pageTotal(userName,email,address);
    //     HashMap<String, Object> map = new HashMap<>();
    //     map.put("data",userList);
    //     map.put("total",total);
    //     return map;
    // }

    // mybatis-plus
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public IPage<User> page(@Parameter(description = "页数") @RequestParam Integer pageNum,
                            @Parameter(description = "页面大小") @RequestParam Integer pageSize,
                            @Parameter(description = "用户名") @RequestParam(value = "userName", required = false) String username,
                            @Parameter(description = "邮箱") @RequestParam(value = "email", required = false) String email,
                            @Parameter(description = "地址") @RequestParam(value = "address", required = false) String address) {

        return userService.page(pageNum, pageSize, username, email, address);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        // 判断id是否存在决定是保存还是更新
        if (user.getId() == null) {
            user.setPassword("123456");
            return Result.success(userService.save(user));
        }
        return Result.success(userService.updateById(user));
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Boolean update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete/{ids}")
    public Boolean delete(@PathVariable("ids") List<Long> ids) {
        return userService.delete(ids);
    }

    /**
     * 导出
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        userService.export(response);
    }

    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws IOException {
        return userService.imp(file);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO)  {
        return Result.success(userService.login(userDTO));
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user)  {
        return Result.success(userService.register(user));
    }

    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",role);
        return Result.success(userService.list(queryWrapper));
    }

}
