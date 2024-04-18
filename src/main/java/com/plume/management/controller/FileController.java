package com.plume.management.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.plume.management.mapper.FileMapper;
import com.plume.management.pojo.FileDB;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Path;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件操作", description = "文件的一系列操作")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Resource
    private FileMapper fileMapper;

    @GetMapping("/test")
    public String test() {
        return "success";
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize() / 1024;

        // 定义一个唯一的文件标识码
        String uuid = IdUtil.fastSimpleUUID();
        File uploadFile = new File(fileUploadPath + uuid + StrUtil.DOT + type);

        // 判断目录是否存在
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String md5;
        String url;
        // 上传文件到磁盘
        file.transferTo(uploadFile);
        // 获取文件md5
        md5 = SecureUtil.md5(uploadFile);
        // 查询md5是否存在
        LambdaQueryWrapper<FileDB> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileDB::getMd5, md5);
        if (!fileMapper.selectList(queryWrapper).isEmpty()) {
            // 删除已上传的重复文件
            FileUtil.del(uploadFile);
            return url = fileMapper.selectList(queryWrapper).get(0).getUrl();
        } else {
            url = "http://localhost:8080/file/" + uuid + StrUtil.DOT + type;
        }

        // 存储数据库
        FileDB fileDB = new FileDB();
        fileDB.setName(originalFilename);
        fileDB.setType(type);
        fileDB.setSize(size);
        fileDB.setUrl(url);
        fileDB.setMd5(md5);

        fileMapper.insert(fileDB);
        return url;
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        File file = new File(fileUploadPath + fileName);
        // 设置输出格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");

        // 读取文件字节流
        outputStream.write(FileUtil.readBytes(file));
        outputStream.flush();
        outputStream.close();

    }

}
