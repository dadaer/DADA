package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.Message;
import com.example.demo.libEntity.Current;
import com.example.demo.libEntity.History;
import com.example.demo.libEntity.Number;
import com.example.demo.service.ConnectService.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lib")
public class LibController {

    @Autowired
    private LibService libService;

    //获取验证码

    @RequestMapping("/init")
    public Message connectIndex(HttpSession httpSession){
        return libService.connectIndex(httpSession);
    }

    @RequestMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession httpSession) throws IOException {
        libService.getCaptcha(response,httpSession);
    }

    //登录且获得数字信息
    @RequestMapping("/number")
    public Number getNumber(String stuNum,String password,String captcha,HttpSession httpSession) throws IOException {
        return libService.getNumber(stuNum,password,captcha,httpSession);
    }

    //获取历史借阅
    @RequestMapping("/history")
    public List<History> getHistory(HttpSession httpSession){
        return libService.getHistory(httpSession);
    }

    //获取当前借阅
    @RequestMapping("/current")
    public List<Current> getCurrent(HttpSession httpSession) {
        return libService.getCurrent(httpSession);
    }
}
