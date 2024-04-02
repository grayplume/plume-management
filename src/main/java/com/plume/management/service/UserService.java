package com.plume.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.management.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {

    List<User> selectAll();
    List<User> page(Integer pageNum, Integer pageSize,String userName,String email,String address);
    boolean saveUser(User user);
    boolean update(User user);
    Integer delete(List<Long> ids);
    Integer pageTotal(String userName,String email,String address);
}
