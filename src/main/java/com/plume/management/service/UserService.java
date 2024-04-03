package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.management.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {

    // List<User> selectAll();
    IPage<User> page(Integer pageNum, Integer pageSize, String userName, String email, String address);
    boolean saveUser(User user);
    boolean update(User user);
    boolean delete(List<Long> ids);
    // Integer pageTotal(String userName,String email,String address);
}
