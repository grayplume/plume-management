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
}




