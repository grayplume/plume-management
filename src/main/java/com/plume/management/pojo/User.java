package com.plume.management.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName sys_user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user")
@Schema(description = "用户表")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    // @JsonIgnore
    private String password;

    private String nickname;

    private String email;

    private String phone;

    private String address;

    private Date createTime;

    private String role;

    private static final long serialVersionUID = 1L;
}