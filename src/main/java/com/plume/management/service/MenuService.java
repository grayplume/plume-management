package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.management.pojo.Role;

import java.util.List;

/**
* @author plume86
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2024-04-19 23:28:59
*/
public interface MenuService extends IService<Menu> {

    IPage<Menu> page(Integer pageNum, Integer pageSize, String name);

    // Boolean update(Menu menu);

    Boolean delete(List<Long> ids);

}
