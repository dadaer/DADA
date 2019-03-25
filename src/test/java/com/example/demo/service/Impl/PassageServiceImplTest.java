package com.example.demo.service.Impl;

import com.example.demo.service.PassageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class PassageServiceImplTest {

    @Autowired
    private PassageService passageService;

    @Test
    public void queryPassage() {
        passageService.queryPassage();
    }
}