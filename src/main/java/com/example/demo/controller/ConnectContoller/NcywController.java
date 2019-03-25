package com.example.demo.controller.ConnectContoller;

import com.example.demo.entity.News;
import com.example.demo.service.ConnectService.NcywService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("news")
public class NcywController {

    @Autowired
    private NcywService ncywService;

    @RequestMapping("ncyw")
    public List<News> getNcyw() {
        List<News> list = ncywService.getNcyw();
        return list;
    }


}
