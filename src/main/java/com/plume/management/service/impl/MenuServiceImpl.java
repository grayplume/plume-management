package com.plume.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.pojo.Menu;
import com.plume.management.pojo.Role;
import com.plume.management.service.MenuService;
import com.plume.management.mapper.MenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author plume86
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2024-04-19 23:28:59
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Override
    public IPage<Menu> page(Integer pageNum, Integer pageSize, String name) {
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        return page(page, queryWrapper);
    }

    // @Override
    // public Boolean update(Menu menu) {
    //     return updateById(menu);
    // }

    @Override
    public Boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public List<Menu> findAll(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("id");
        List<Menu> list = list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).toList();
        // 找出一级菜单的子菜单
        for (Menu menu:parentNode) {
            // 筛选所有菜单中pid等于父菜单id的菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNode;
    }
}




