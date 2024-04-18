package com.plume.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.pojo.FileDB;
import com.plume.management.service.FileService;
import com.plume.management.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
* @author plume86
* @description 针对表【sys_file】的数据库操作Service实现
* @createDate 2024-04-18 16:13:28
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDB>
    implements FileService{

}




