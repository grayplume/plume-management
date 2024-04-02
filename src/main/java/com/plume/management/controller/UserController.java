package com.plume.management.controller;

import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name="用户操作",description = "用户的一系列操作")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有用户")
    public List<User> testSelect() {
        return userService.selectAll();
    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Map<String, Object> page(@Parameter(description = "页数")@RequestParam Integer pageNum,
                                    @Parameter(description = "页面大小")@RequestParam Integer pageSize,
                                    @Parameter(description = "用户名") @RequestParam(value = "userName",required = false) String userName,
                                    @Parameter(description = "邮箱")@RequestParam(value = "email",required = false) String email,
                                    @Parameter(description = "地址")@RequestParam(value = "address",required = false) String address){
        pageNum = (pageNum - 1) * pageSize;
        List<User> userList = userService.page(pageNum, pageSize,userName,email,address);
        Integer total = userService.pageTotal(userName,email,address);
        HashMap<String, Object> map = new HashMap<>();
        map.put("data",userList);
        map.put("total",total);
        return map;
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("/save")
    public Boolean save(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Boolean update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @PostMapping("/delete/{ids}")
    public Integer delete(@PathVariable("ids") List<Long> ids) {
        return userService.delete(ids);
    }
}
