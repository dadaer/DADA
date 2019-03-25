package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.NewsDetail;
import com.example.demo.service.ConnectService.JwggDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/news")
public class JwggDetailController {

    @Autowired
    private JwggDetailService jwggDetailService;

    @RequestMapping("/jw_detail")
    public NewsDetail getJwDetail(String src) throws IOException {
        NewsDetail newsDetail = jwggDetailService.getJwDetail(src);
        return newsDetail;
    }
}
