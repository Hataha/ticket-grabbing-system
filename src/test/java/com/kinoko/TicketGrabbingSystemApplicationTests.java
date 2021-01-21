package com.kinoko;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.StudentMapper;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
class TicketGrabbingSystemApplicationTests {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(redisTemplate.opsForValue().get("kill10"));
    }
}
