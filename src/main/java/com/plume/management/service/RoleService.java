package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.management.pojo.User;

import java.util.List;

/**
* @author plume86
* @description 针对表【sys_role】的数据库操作Service
* @createDate 2024-04-19 12:20:42
*/
public interface RoleService extends IService<Role> {

    IPage<Role> page(Integer pageNum, Integer pageSize, String name);

    Boolean update(Role role);

    Boolean delete(List<Long> ids);
}
