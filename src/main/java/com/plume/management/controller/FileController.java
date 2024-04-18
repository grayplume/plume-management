package com.plume.management.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.plume.management.common.Result;
import com.plume.management.mapper.FileMapper;
import com.plume.management.pojo.FileDB;
import com.plume.management.pojo.User;
import com.plume.management.service.FileService;
import com.plume.management.utils.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Path;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件操作", description = "文件的一系列操作")
public class FileController {


    @Autowired
    private FileService fileService;
    @GetMapping("/test")
    public String test() {
        return "success";
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    @GetMapping("/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        fileService.download(fileName, response);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    public IPage<FileDB> page(@Parameter(description = "页数") @RequestParam Integer pageNum,
                            @Parameter(description = "页面大小") @RequestParam Integer pageSize,
                            @Parameter(description = "文件名") @RequestParam(value = "name", required = false) String name
                            ) {
        return fileService.page(pageNum, pageSize, name);
    }

    @PostMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return fileService.delete(ids);
    }

    @PostMapping("/update")
    public Result save(@RequestBody FileDB fileDB) {
        return Result.success(fileService.updateById(fileDB));
    }

}
