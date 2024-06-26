package com.plume.management.mapper;

import com.plume.management.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author plume86
* @description 针对表【sys_role】的数据库操作Mapper
* @createDate 2024-04-19 12:20:42
* @Entity com.plume.management.pojo.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}




