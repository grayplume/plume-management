package com.plume.management.service;

import com.plume.management.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> selectAll();
    List<User> page(Integer pageNum, Integer pageSize);
    Integer save(User user);
    Integer update(User user);
    Integer delete(List<Long> ids);
    Integer pageTotal();
}
