package com.kinoko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.entity.TicketRecord;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.StudentMapper;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper,Activity> implements ActivityService  {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private TicketRecordMapper ticketRecordMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public List<Student> getAllStudentsByActivityId(int aid) {

        List<TicketRecord> records = ticketRecordMapper
                .selectList(new QueryWrapper<TicketRecord>()
                .eq("a_id", aid));
        List<String> sids = records.stream()
                .map(TicketRecord::getSid)
                .collect(Collectors.toList());
        if(sids.size() == 0)
            return null;
        return studentMapper.selectBatchIds(sids);
    }

    @Override
    public boolean deleteActivity(int aid) {
        ticketRecordMapper.delete(new QueryWrapper<TicketRecord>()
                .eq("a_id", aid));
        //从redis缓存中删除记录
        redisTemplate.delete("kill" + aid);
        return activityMapper.deleteById(aid) != 0;
    }

    @Override
    public boolean addActivity(Map<String, Object> params) {
        String startTimeStr = ((String) params.get("startTime")).replace('T',' ');
        String startStr = ((String) params.get("start")).replace('T',' ');
        int rest = Integer.parseInt((String) params.get("rest"));
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTimeStr);
            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startStr);
            Activity activity = new Activity();
            activity.setName((String)params.get("name"));
            activity.setStartTime(startTime);
            activity.setDetail((String)params.get("detail"));
            activity.setStart(start);
            activity.setRest(rest);
            //存入mysql数据库
            activityMapper.insert(activity);
            //活动开始时间存入redis
            //这里mybatisplus插入activity后可以直接通过getId获取到主键的值
            activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("name",activity.getName()));
            redisTemplate.opsForValue().set("kill" + activity.getId(), String.valueOf(activity.getStartTime().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void modifyActivity(int aid, Map<String, Object> params) {
        String startTimeStr = ((String) params.get("startTime")).replace('T',' ');
        String startStr = ((String) params.get("start")).replace('T',' ');
        int rest = Integer.parseInt((String) params.get("rest"));
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTimeStr);
            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startStr);
            Activity activity = new Activity();
            activity.setId(aid);
            activity.setName((String)params.get("name"));
            activity.setStartTime(startTime);
            activity.setDetail((String)params.get("detail"));
            activity.setStart(start);
            activity.setRest(rest);
            activityMapper.updateById(activity);

            redisTemplate.opsForValue().set("kill" + activity.getId(), String.valueOf(activity.getStartTime().getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    @Autowired
//    private ActivityMapper activityMapper;
//    @Autowired
//    private StudentMapper studentMapper;
//
//    @Override
//    public List<Activity> selectAllActivities() {
//        return activityMapper.selectAllActivities();
//    }
//
//    @Override
//    public Activity selectActivityById(int id) {
//        return activityMapper.selectActivityById(id);
//    }
//
//    @Override
//    public int addActivity(Map<String,Object> activityParamMap) {
//        String startTimeStr = ((String) activityParamMap.get("startTime")).replace('T',' ');
//        String startStr = ((String) activityParamMap.get("start")).replace('T',' ');
//        int rest = Integer.parseInt((String) activityParamMap.get("rest"));
//        try {
//            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTimeStr);
//            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startStr);
//            Activity activity = new Activity((String)activityParamMap.get("name"),
//                    startTime, (String)activityParamMap.get("detail"), start, rest);
//            return activityMapper.addActivity(activity);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    @Override
//    public List<Student> selectAllStudentsByActivityId(int id) {
//        return studentMapper.selectAllStudentsByActivityId(id);
//    }
//
//    @Override
//    public int removeStudentFromActivity(int aid, String sid) {
//        return activityMapper.removeStudentFromActivity(aid,sid);
//    }
//
//    @Override
//    public int updateActivity(int id, Map<String, Object> activityParamMap) {
//        Activity activity = activityMapper.selectActivityById(id);
//        String startTimeStr = ((String) activityParamMap.get("startTime")).replace('T',' ');
//        String startStr = ((String) activityParamMap.get("start")).replace('T',' ');
//        int rest = Integer.parseInt((String) activityParamMap.get("rest"));
//        try {
//            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTimeStr);
//            Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startStr);
//            activity.setName((String) activityParamMap.get("name"));
//            activity.setStartTime(startTime);
//            activity.setDetail((String) activityParamMap.get("detail"));
//            activity.setStart(start);
//            activity.setRest(rest);
//            return activityMapper.updateActivity(activity);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    @Override
//    public int deleteActivity(int id) {
//        activityMapper.deleteActivityById(id);
//        return activityMapper.deleteSAInfoByAId(id);
//    }
}
