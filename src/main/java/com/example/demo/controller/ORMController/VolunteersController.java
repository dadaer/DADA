package com.example.demo.controller.ORMController;

import com.example.demo.ORMEntity.Volunteers;
import com.example.demo.service.VolunteersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class VolunteersController {

    @Autowired
    private VolunteersService volunteersService;

    @RequestMapping("volunteers")
    public List<Volunteers> queryVolunteers() {
        return volunteersService.queryVolunteers();
    }

    @RequestMapping("volunteer")
    public Volunteers queryVolunteersById(Integer id){
        return volunteersService.queryVolunteersById(id);
    }
}
