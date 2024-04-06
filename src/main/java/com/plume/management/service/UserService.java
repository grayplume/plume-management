package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.plume.management.pojo.User;
import com.plume.management.pojo.dto.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


public interface UserService extends IService<User> {

    // List<User> selectAll();
    IPage<User> page(Integer pageNum, Integer pageSize, String userName, String email, String address);
    boolean saveUser(User user);
    boolean update(User user);
    boolean delete(List<Long> ids);
    void export(HttpServletResponse response) throws IOException;

    Boolean imp(MultipartFile file) throws IOException;

    Boolean login(UserDTO userDTO);
    // Integer pageTotal(String userName,String email,String address);
}
