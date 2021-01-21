package com.kinoko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.entity.TicketRecord;
import com.kinoko.dao.ActivityMapper;
import com.kinoko.dao.StudentMapper;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private TicketRecordMapper ticketRecordMapper;

    @Override
    public List<Activity> getAllActivitiesOfStudent(String sid) {

        List<TicketRecord> records = ticketRecordMapper
                .selectList(new QueryWrapper<TicketRecord>()
                .eq("s_id",sid));
        //将TicketRecord集合中的aid属性抽取出来作为list
        List<Integer> aids = records.stream()
                .map(TicketRecord::getAid)
                .collect(Collectors.toList());
        //如果不加判断，当aids为空的时候selectBatchIds会报错
        if(aids.size() == 0)
            return null;
        return activityMapper.selectBatchIds(aids);
    }

    @Override
    public void changeLock(String sid) {
        Student student = studentMapper.selectById(sid);
        student.setLocked(student.getLocked() == 0 ? 1 : 0);
        studentMapper.updateById(student);
    }
}
