package com.plume.management.pojo.dto;

import com.plume.management.pojo.Menu;
import lombok.Data;

import java.util.List;

/**
 * 登录传输实体
 */
@Data
public class UserDTO {
    private Integer id;
    private String userName;
    private String password;
    private String nickname;
    private String token;
    private String role;
    private List<Menu> menus;
}
