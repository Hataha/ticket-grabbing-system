package com.kinoko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ActivityService extends IService<Activity> {
    /**
     * 查询所有选择了某活动的学生
     * @param aid 活动id
     * @return 选择了此活动的学生列表
     */
    List<Student> getAllStudentsByActivityId(int aid);

    /**
     * 根据活动id删除活动，要删除活动本身和所有与此活动有关的记录
     * 需要删除redis缓存中的记录
     * @param aid 活动id
     * @return 是否存在此活动被删除，活动如果本身不存在则返回false
     */
    boolean deleteActivity(int aid);

    /**
     * 添加一个活动，待改进。可以由前端的表单的参数直接对应到实体类
     * 包括将活动的信息存入数据库，活动id和活动开始时间对存入redis缓存
     * @param params 从前端接收过来的参数
     * @return 添加成功则返回true，否则返回false
     */
    boolean addActivity(Map<String,Object> params);

    /**
     * 修改活动信息
     * @param aid 活动id
     * @param params 从表单接收的活动参数
     */
    void modifyActivity(int aid,Map<String,Object> params);
}
