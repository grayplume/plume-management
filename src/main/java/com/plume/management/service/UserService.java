package com.plume.management.service;

import com.plume.management.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> selectAll();
    Integer save(User user);
    Integer update(User user);
    Integer delete(List<Long> ids);
}