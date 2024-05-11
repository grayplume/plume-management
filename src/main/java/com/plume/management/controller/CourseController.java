package com.plume.management.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plume.management.common.Constants;
import com.plume.management.common.Result;
import com.plume.management.config.AuthAccess;
import com.plume.management.pojo.Course;
import com.plume.management.pojo.Dict;
import com.plume.management.pojo.Menu;
import com.plume.management.service.CourseService;
import com.plume.management.service.DictService;
import com.plume.management.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/findAll")
    // @AuthAccess
    public Result findAll() {
        return Result.success(courseService.findAll());
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public Result page(@Parameter(description = "页数") @RequestParam Integer pageNum,
                       @Parameter(description = "页面大小") @RequestParam Integer pageSize,
                       @Parameter(description = "角色名称") @RequestParam(value = "name", required = false) String name) {
        Page<Course> page = courseService.findPage(new Page<>(pageNum, pageSize), name);
        return Result.success(page);
    }

    @PostMapping("/save")
    public Result save(@RequestBody Course course) {
        // 判断id是否存在决定是保存还是更新
        if (course.getId() == null) {
            return Result.success(courseService.save(course));
        }
        return Result.success(courseService.updateById(course));
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return Result.success(courseService.delete(ids));
    }

    @GetMapping("/name/{name}")
    public Result findOne(@PathVariable String name) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return Result.success(courseService.getOne(queryWrapper));
    }

    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public Result studentCourse(@PathVariable Integer courseId,@PathVariable Integer studentId){
        courseService.setStudentCourse(courseId,studentId);
        return Result.success();
    }


}
