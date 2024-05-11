package com.plume.management.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plume.management.exception.LoginFailedException;
import com.plume.management.mapper.RoleMapper;
import com.plume.management.mapper.RoleMenuMapper;
import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.Menu;
import com.plume.management.pojo.User;
import com.plume.management.pojo.dto.UserDTO;
import com.plume.management.service.MenuService;
import com.plume.management.service.UserService;
import com.plume.management.utils.TokenUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // @Autowired
    // private UserMapper userMapper;

    private static final Log LOG = Log.get();

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserMapper userMapper;


    @Override
    public IPage<User> page(Integer pageNum, Integer pageSize, String username, String email, String address) {
        // 手写方法 XML动态SQL
        // return userMapper.page(pageNum,pageSize,userName,email,address);
        // 插件实现
        IPage<User> page = new Page<>(pageNum, pageSize);
        // QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // queryWrapper.like(StringUtils.isNotBlank(userName), "username", userName)
        //         .like(StringUtils.isNotBlank(email), "email", email)
        //         .like(StringUtils.isNotBlank(address), "address", address);
        return userMapper.findPage(page, username,email,address);
    }

    @Override
    public boolean saveUser(User user) {


        // 判断用户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User one = getOne(queryWrapper);

        if (one != null) {
            throw new LoginFailedException("用户已存在");
        }
        if (user.getPassword() == null){
            user.setPassword("123456");
        }

        if (user.getNickname() == null){
            user.setNickname("无名氏");
        }

        return save(user);

    }

    private boolean updateUser(User user) {
        // 在此处实现具体的用户更新逻辑，例如通过 ID 查询数据库并更新用户信息
        // 假设 update 方法返回布尔值表示更新是否成功
        return update(user);
    }

    @Override
    public boolean update(User user) {
        return updateById(user);
        // return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        // 从数据库查询所有数据
        List<User> list = list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("id", "id");
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");

        // 一次性写出list到excel,使用默认样式,强制输出标题
        writer.write(list, true);

        // 设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    @Override
    public Boolean imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过java bean方式读取excel的对象 要求表头必须是跟java bean的属性对应起来
        // List<User> list = reader.readAll(User.class);

        // 忽略表头的中文,直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> userList = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            userList.add(user);
        }
        return saveBatch(userList);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {

        if (StringUtils.isBlank(userDTO.getUserName()) || StringUtils.isBlank(userDTO.getPassword())) {
            throw new LoginFailedException("参数异常");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUserName());
        queryWrapper.eq("password", userDTO.getPassword());
        User one = null;
        try {
            one = getOne(queryWrapper);
            // 生成用户Token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            UserDTO dto = new UserDTO();
            dto.setId(one.getId());
            dto.setUserName(one.getUsername());
            dto.setPassword(one.getPassword());
            dto.setNickname(one.getNickname());
            dto.setToken(token);
            String role = one.getRole();  //ROLE_ADMIN
            dto.setRole(role);

            ArrayList<Menu> roleMenus = getRoleMenus(role);

            dto.setMenus(roleMenus);
            return dto;
        } catch (Exception e) {
            LOG.error(e);
            throw new LoginFailedException("用户名或密码错误");
        }

    }

    /**
     * 获取当前角色菜单列表
     * @param role
     * @return
     */
    private ArrayList<Menu> getRoleMenus(String role) {
        Integer roleId = roleMapper.selectByFlag(role);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        List<Menu> menuList = menuService.findAll("");

        ArrayList<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户菜单
        for (Menu menu : menuList) {
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            menu.getChildren().removeIf(item -> !menuIds.contains(item.getId()));
        }
        return roleMenus;
    }

    @Override
    public Boolean register(User user) {
        return saveUser(user);
    }

    // @Override
    // public Integer pageTotal(String userName, String email, String address) {
    //     return userMapper.pageTotal(userName, email, address);
    // }
}
