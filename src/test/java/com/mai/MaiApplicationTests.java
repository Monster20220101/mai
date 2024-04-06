package com.mai;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mai.entity.User;
import com.mai.mapper.UserMapper;
import com.mai.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MaiApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void test() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        System.out.println(id);
    }

    @Test
    void queryUser() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("create_time");
        List<User> users = userMapper.selectList(wrapper);

        System.out.println("\n\n");
        users.forEach(System.out::println);
        System.out.println("\n\n");
    }

    @Test
    void addUser() {
        User user = new User();
        user.setUserId("2");
        user.setUserPassword("2");
        user.setUserEmail("11@");
        int result = userMapper.insert(user); // 帮我们自动生成id
        System.out.println(result); // 受影响的行数
        System.out.println(user); // 发现，id会自动回填
    }

    @Test
    public void testUpdate() {
        User user = new User();
        // 通过条件自动拼接动态sql
        user.setUserId("22");
        user.setUserNickname("关注公众号");
        user.setUserEmail("18");
        int i = userMapper.update(user, null);
        // updateById能自动搞 我这个想updateByUserId的根本搞不懂
        System.out.println(i);
    }

    // 按条件查询之一使用map操作
    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        // 自定义要查询
        map.put("user_flag", "0"); // 这里写成_下划线版本
        //map.put("age",3); // 多条件查询
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    public void testPage() {
        Page<User> page = new Page<>(1, 2); //(当前页,页面大小)
        // 可以setCurrent setSize
        userMapper.selectPage(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
        // page.getPages()总页数
        // page.getTotal()总数据数
        // page.getCurrent()当前页数
        // page.getRecords()获取分页后的数据
        // page.hasNext()是否有下一页
        // page.hasPrevious()是否有上一页
    }

    // 删除userMapper.deleteByMap(map)
}
