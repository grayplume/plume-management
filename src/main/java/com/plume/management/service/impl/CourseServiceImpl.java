package com.plume.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.pojo.Course;
import com.plume.management.pojo.Menu;
import com.plume.management.service.CourseService;
import com.plume.management.mapper.CourseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author plume86
* @description 针对表【course】的数据库操作Service实现
* @createDate 2024-04-22 21:29:37
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> findAll() {
        return courseMapper.selectList(null);
    }

    @Override
    public IPage<Course> page(Integer pageNum, Integer pageSize, String name) {
        IPage<Course> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        return page(page, queryWrapper);
    }

    @Override
    public Boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Page<Course> findPage(Page<Course> coursePage, String name) {

        return courseMapper.findPage(coursePage,name);
    }

    @Override
    @Transactional()
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId,studentId);
        courseMapper.setStudentCourse(courseId, studentId);
    }
}




