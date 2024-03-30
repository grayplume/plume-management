package com.plume.management.controller;

import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/list")
    public List<User> testSelect() {
        System.out.println(("----- selectAll method  ------"));
        List<User> userList = userService.selectAll();
        return userList;
    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping("/page")
    public Map<String, Object> page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        List<User> userList = userService.page(pageNum, pageSize);
        Integer total = userService.pageTotal();
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
    public Integer save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Integer update(@RequestBody User user) {
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
