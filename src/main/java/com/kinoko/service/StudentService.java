package com.kinoko.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService extends IService<Student> {
    /**
     * 查看学生已选择的活动列表
     * @param sid 学号
     * @return 已选活动列表
     */
    List<Activity> getAllActivitiesOfStudent(String sid);

    /**
     * 锁定或解锁学生
     */
    void changeLock(String sid);
}
