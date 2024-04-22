package com.plume.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.mapper.RoleMenuMapper;
import com.plume.management.pojo.Menu;
import com.plume.management.pojo.Role;
import com.plume.management.pojo.RoleMenu;
import com.plume.management.pojo.User;
import com.plume.management.service.MenuService;
import com.plume.management.service.RoleMenuService;
import com.plume.management.service.RoleService;
import com.plume.management.mapper.RoleMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author plume86
 * @description 针对表【sys_role】的数据库操作Service实现
 * @createDate 2024-04-19 12:20:42
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuService menuService;

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

    @Override
    @Transactional
    public Boolean roleMenu(Integer roleId, List<Integer> menuIds) {
        // 先删除id绑定的所有关系
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        roleMenuMapper.delete(queryWrapper);

        // 临时集合存放待插入的父节点
        Set<Integer> pendingParentIds = new HashSet<>();

        for(Integer menuId : menuIds){
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIds.contains(menu.getPid())){  // 二级菜单  并且ids里面没有父节点
                // 将父节点添加到临时集合，后续统一处理
                pendingParentIds.add(menu.getPid());
            }

            // 插入当前菜单（无论一级还是二级）
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);

            // 处理缺失的父节点（仅插入一次）
            for (Integer parentId : pendingParentIds) {
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(parentId);
                roleMenuMapper.insert(roleMenu);
            }
        }
        return null;
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}




