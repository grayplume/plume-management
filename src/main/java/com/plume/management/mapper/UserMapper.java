package com.plume.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author plume86
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-03-30 15:42:25
* @Entity com.plume.management.pojo.User
*/
// @Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> findPage(IPage<User> page, String username, String email, String address);

    // List<User> selectAll();

    // List<User> page(Integer pageNum, Integer pageSize,String userName,String email,String address);

    // int deleteByPrimaryKeys(List<Long> ids);
    //
    // int insertSelective(User record);
    //
    // User selectByPrimaryKey(Long id);
    //
    // int updateByPrimaryKeySelective(User record);
    //
    // int pageTotal(String userName,String email,String address);
}
