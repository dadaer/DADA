package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.News;
import com.example.demo.service.ConnectService.JwggService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/news")
public class JwggController {

    @Autowired
    private JwggService jwggService;

    @RequestMapping("/jwgg")
    public List<News> getJwgg(){
        List<News> news = jwggService.getJwgg();
        return news;
    }
}

