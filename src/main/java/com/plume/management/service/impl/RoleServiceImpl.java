package com.plume.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.pojo.Role;
import com.plume.management.pojo.User;
import com.plume.management.service.RoleService;
import com.plume.management.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author plume86
 * @description 针对表【sys_role】的数据库操作Service实现
 * @createDate 2024-04-19 12:20:42
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public IPage<Role> page(Integer pageNum, Integer pageSize, String name) {
        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        return page(page, queryWrapper);
    }

    @Override
    public Boolean update(Role role) {
        return updateById(role);
    }

    @Override
    public Boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }
}




