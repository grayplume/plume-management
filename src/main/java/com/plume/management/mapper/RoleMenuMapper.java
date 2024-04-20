package com.plume.management.mapper;

import com.plume.management.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author plume86
* @description 针对表【sys_role_menu】的数据库操作Mapper
* @createDate 2024-04-20 16:17:47
* @Entity com.plume.management.pojo.RoleMenu
*/
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId")Integer roleId);
}




