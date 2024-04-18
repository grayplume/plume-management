package com.plume.management.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName sys_file
 */
@Data
@TableName(value = "sys_file")
public class FileDB implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private Long size;

    private String url;

    private String md5;

    private Integer isDelete;

    private Boolean enable;

    private static final long serialVersionUID = 1L;
}