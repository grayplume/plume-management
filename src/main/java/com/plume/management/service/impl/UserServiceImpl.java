package com.plume.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // @Autowired
    // private UserMapper userMapper;


    @Override
    public IPage<User> page(Integer pageNum, Integer pageSize, String userName, String email, String address) {
        // 手写方法 XML动态SQL
        // return userMapper.page(pageNum,pageSize,userName,email,address);
        // 插件实现
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userName),"username",userName)
                .like(StringUtils.isNotBlank(email),"email",email)
                .like(StringUtils.isNotBlank(address),"address",address);
        return page(page,queryWrapper);
    }

    @Override
    public boolean saveUser(User user) {
        // 判断用户是否已存在（即是否有 ID）
        if (user.getUsername() != null) {
            // 用户已存在，执行更新操作
            return updateUser(user);
        } else {
            // 用户为新用户，设置默认密码并执行保存操作
            user.setPassword("123456");
            return save(user);
        }
    }

    private boolean updateUser(User user) {
        // 在此处实现具体的用户更新逻辑，例如通过 ID 查询数据库并更新用户信息
        // 假设 update 方法返回布尔值表示更新是否成功
        return update(user);
    }

    @Override
    public boolean update(User user) {
        return updateById(user);
        // return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    // @Override
    // public Integer pageTotal(String userName, String email, String address) {
    //     return userMapper.pageTotal(userName, email, address);
    // }
}
