package com.example.demo.service.Impl;

import com.example.demo.ORMEntity.Volunteers;
import com.example.demo.dao.VolunteersMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class VolunteersServiceImplTest {

    @Autowired
    private VolunteersMapper volunteersMapper;


    @Test
    public void queryVolunteers() {

    }

    @Test
    public void queryVolunteersById() {

    }
}