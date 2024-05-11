package com.plume.management.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plume.management.pojo.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author plume86
* @description 针对表【course】的数据库操作Mapper
* @createDate 2024-04-22 21:29:37
* @Entity com.plume.management.pojo.Course
*/
public interface CourseMapper extends BaseMapper<Course> {

    Page<Course> findPage(Page<Course> coursePage, String name);

    void deleteStudentCourse(Integer courseId, Integer studentId);
    void setStudentCourse(Integer courseId, Integer studentId);
}




