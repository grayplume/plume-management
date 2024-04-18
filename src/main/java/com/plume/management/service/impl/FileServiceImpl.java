package com.plume.management.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.common.Result;
import com.plume.management.pojo.FileDB;
import com.plume.management.pojo.User;
import com.plume.management.service.FileService;
import com.plume.management.mapper.FileMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
* @author plume86
* @description 针对表【sys_file】的数据库操作Service实现
* @createDate 2024-04-18 16:13:28
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDB>
    implements FileService{


    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Resource
    private FileMapper fileMapper;

    @Override
    public Result upload(MultipartFile file) throws IOException {
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
            return Result.success(fileMapper.selectList(queryWrapper).get(0).getUrl());
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
        return Result.success(url);
    }

    @Override
    public void download(String fileName, HttpServletResponse response) throws IOException {
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

    @Override
    public IPage<FileDB> page(Integer pageNum, Integer pageSize, String name) {
        IPage<FileDB> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FileDB> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name)
                .eq("is_delete",0);
        return page(page, queryWrapper);
    }

    @Override
    public Result delete(List<Long> ids) {
        List<FileDB> fileDBS = fileMapper.selectBatchIds(ids);
        if (!fileDBS.isEmpty()) {
            for (FileDB fileDB : fileDBS) {
                fileDB.setIsDelete(1);
                fileMapper.updateById(fileDB);
            }
        }
        return Result.success();
    }
}




