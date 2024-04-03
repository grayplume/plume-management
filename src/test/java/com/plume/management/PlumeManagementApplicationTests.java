package com.plume.management;

import com.plume.management.mapper.UserMapper;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class PlumeManagementApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void testDelete() {
        ArrayList<Long> ids = new ArrayList<Long>();
        List<Long> moreLongs = List.of(4L, 5L);
        ids.addAll(moreLongs);

        boolean delete = userService.delete(ids);
        System.out.println(delete);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(3);
        user.setUsername("test3");
        user.setNickname("test3");
        boolean update = userService.update(user);
        System.out.println(update);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setPassword("123456");
        user.setNickname("test");
        user.setEmail("test@qq.com");
        user.setPhone("123456789");
        user.setAddress("test");
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            Random random = new Random(100);
            int i1 = random.nextInt();
            user.setUsername("test"+i1);
            list.add(user);
        }
        userService.saveBatch(list);
    }

    // @Test
    // void testSelect() {
    //     System.out.println(("----- selectAll method test ------"));
    //     List<User> userList = userService.selectAll();
    //     System.out.println(userList);
    // }



    @Test
    void contextLoads() {
    }

}
