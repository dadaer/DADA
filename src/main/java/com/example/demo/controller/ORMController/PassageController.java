package com.example.demo.controller.ORMController;

import com.example.demo.service.PassageService;
import com.example.demo.ORMEntity.Passage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PassageController {

    @Autowired
    private PassageService passageservice;

    @RequestMapping("listPassage")
    public List<Passage> listPassage(){
        List<Passage> list = passageservice.queryPassage();
        return list;
    }
}
