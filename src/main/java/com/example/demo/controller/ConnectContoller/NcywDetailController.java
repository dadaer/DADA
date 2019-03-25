package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.NewsDetail;
import com.example.demo.service.ConnectService.NcywDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NcywDetailController {

    @Autowired
    private NcywDetailService ncywDetailService;

    @RequestMapping("/yw_detail")
    public NewsDetail getYwDetail(String src) {
        NewsDetail newsDetail = ncywDetailService.getYwDetail(src);
        return newsDetail;
    }
}

