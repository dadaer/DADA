package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.*;
import com.example.demo.service.ConnectService.JwglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/management")
public class JwglController {

    @Autowired
    private JwglService jwglService;

    //
    @RequestMapping("/init")
    public Message ConnectIndex(HttpSession httpSession){
        return jwglService.connectIndex(httpSession);
    }
    //下载验证码到本地
    @RequestMapping("/checkCode")
    public void getCheckCode(HttpServletResponse res,HttpSession httpSession)
            throws IOException {
        jwglService.getCheckCode(res,httpSession);
    }

    //登录
    @RequestMapping("/login")
    public Message login(String stuNum, String password, String checkCode,
                         String stuName,HttpSession httpSession) throws Exception {
        return jwglService.login(stuNum,password,checkCode,stuName,httpSession);
    }

    //获取姓名
    @RequestMapping("/info")
    public String getInfo(){
        return jwglService.getInfo();
    }

    //获取课表
    @RequestMapping("course")
    public List<Course> getCourse(HttpSession httpSession) throws IOException {
        return jwglService.getCourse(httpSession);
    }

    //获取学生成绩
    @RequestMapping("grade")
    @ResponseBody
    public List<Grade> getGrade(HttpSession httpSession) throws IOException {
        return jwglService.getGrade(httpSession);
    }

    //获取考试安排
    @RequestMapping("exam")
    public List<Exam> getExam(HttpSession httpSession) {
        return jwglService.getExam(httpSession);
    }

    //获取等级考试
    @RequestMapping("cet")
    public List<CET> getCET(HttpSession httpSession) {
        return jwglService.getCET(httpSession);
    }

    //获取空教室
    @RequestMapping("classroom")
    public String[] getClassroom(HttpSession httpSession) {
        return jwglService.getClassroom(httpSession);
    }
}