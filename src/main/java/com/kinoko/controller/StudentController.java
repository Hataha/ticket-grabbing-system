package com.kinoko.controller;

import com.kinoko.entity.Activity;
import com.kinoko.entity.Student;
import com.kinoko.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/student/showStudents")
    public String showStudents(Model model){
        List<Student> students = studentService.list();
        model.addAttribute("students",students);
        model.addAttribute("isManagePage",true);
        return "student/studentList";
    }



    @GetMapping("/student/lock/{id}")
    public String changeLock(@PathVariable("id") String id,
                             RedirectAttributes model){
        studentService.changeLock(id);
        model.addFlashAttribute("msg","修改锁定状态成功");
        return "redirect:/student/showStudents";
    }

    @GetMapping("/student/activities/{id}")
    public String showActivitiesOfStudent(@PathVariable("id") String id,
                                          Model model){
        List<Activity> activities = studentService.getAllActivitiesOfStudent(id);
        model.addAttribute("activities",activities);
        //用来标识是否是活动管理页面
        model.addAttribute("isManagePage",false);
        return "activity/activityList";
    }
}
