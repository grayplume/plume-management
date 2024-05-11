package com.plume.management.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName course
 */
@TableName(value ="course")
@Data
public class Course implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 学分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 上课时间
     */
    @TableField(value = "times")
    private String times;

    /**
     * 是否开课
     */
    @TableField(value = "state")
    private Boolean state;

    /**
     * 授课老师id
     */
    @TableField(value = "teacher_id")
    private Integer teacherId;

    @TableField(exist = false)
    private String teacher;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}