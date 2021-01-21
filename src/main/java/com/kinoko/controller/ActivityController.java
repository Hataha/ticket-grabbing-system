package com.kinoko.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.entity.TicketRecord;
import com.kinoko.service.ActivityService;
import com.kinoko.service.StudentService;
import com.kinoko.service.TicketRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TicketRecordService ticketRecordService;

    @GetMapping("/activity/showActivities")
    public String showActivities(Model model){
        List<Activity> activities = activityService.list();
        model.addAttribute("activities",activities);
        model.addAttribute("isManagePage",true);
        return "activity/activityList";
    }

    @GetMapping("/activity/toDetailPage/{id}")
    public String toDetailPage(@PathVariable("id") int id,
                               Model model){
        Activity activity = activityService.getById(id);
        List<Student> students = activityService.getAllStudentsByActivityId(id);
        model.addAttribute("activity",activity);
        model.addAttribute("students",students);
        return "activity/activityDetail";
    }

    @GetMapping("/activity/toAddPage")
    public String toDetailPage(){
        return "activity/activityAdd";
    }

    @PostMapping("/activity/doAdd")
    public String doAdd(@RequestParam Map<String,Object> params,
                        RedirectAttributes model){
        if(!activityService.addActivity(params))
            model.addFlashAttribute("msg","添加活动失败");
        else
            model.addFlashAttribute("msg","添加活动成功");
        return "redirect:/activity/showActivities";
    }

    @GetMapping("/activity/removeFromActivity/{aid}/{sid}")
    public String removeFromActivity(@PathVariable("aid") int aid,
                                     @PathVariable("sid") String sid,
                                     RedirectAttributes model){
        if(!ticketRecordService.remove(new QueryWrapper<TicketRecord>()
                .eq("s_id",sid).eq("a_id",aid))){
            //通常情况下不会进入此语句，除非直接调用API接口
            model.addFlashAttribute("msg","此学生未选择此活动");
        };
        Activity activity = activityService.getById(aid);
        activityService.update(activity,new UpdateWrapper<Activity>().set("rest",activity.getRest() + 1));
        return "redirect:/activity/toDetailPage/"+aid;
    }

    @GetMapping("activity/toModifyPage/{id}")
    public String toModifyPage(@PathVariable("id") int id, Model model){
        Activity activity = activityService.getById(id);
        model.addAttribute("activity",activity);
        return "activity/activityModify";
    }

    @PostMapping("activity/doModify/{id}")
    public String doModify(@RequestParam Map<String,Object> params,
                           @PathVariable("id") int id,
                           RedirectAttributes model){
        activityService.modifyActivity(id,params);
        model.addFlashAttribute("msg","修改成功");
        return "redirect:/activity/toDetailPage/" + id;
    }

    @GetMapping("/activity/doDelete/{id}")
    public String doDelete(@PathVariable("id") int id,
                           RedirectAttributes model){
        if(activityService.deleteActivity(id)){
            model.addFlashAttribute("msg","活动删除成功");
        }
        else{
            //正常情况下不会进入此语句，除非直接调用API接口
            model.addFlashAttribute("msg","不存在此活动");
        }
        return "redirect:/activity/showActivities";
    }
}
