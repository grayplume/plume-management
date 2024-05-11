package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plume.management.pojo.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author plume86
* @description 针对表【course】的数据库操作Service
* @createDate 2024-04-22 21:29:37
*/
public interface CourseService extends IService<Course> {

    List<Course> findAll();

    IPage<Course> page(Integer pageNum, Integer pageSize, String name);

    Boolean delete(List<Long> ids);

    Page<Course> findPage(Page<Course> coursePage, String name);

    void setStudentCourse(Integer courseId, Integer studentId);
}
