package com.plume.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.common.Result;
import com.plume.management.pojo.FileDB;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author plume86
* @description 针对表【sys_file】的数据库操作Service
* @createDate 2024-04-18 16:13:28
*/
public interface FileService extends IService<FileDB> {

    Result upload(MultipartFile file) throws IOException;

    void download(String fileName, HttpServletResponse response) throws IOException;

    IPage<FileDB> page(Integer pageNum, Integer pageSize, String name);

    Result delete(List<Long> ids);
}
