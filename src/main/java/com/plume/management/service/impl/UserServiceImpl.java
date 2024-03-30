package com.plume.management.service.impl;

import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public Integer save(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public Integer update(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer delete(List<Long> ids) {
        return userMapper.deleteByPrimaryKeys(ids);
    }
}
