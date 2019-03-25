package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;


@RestController
public class ImageController {
    @RequestMapping("/image")
    public static void getImage(HttpServletResponse response,String imgUrl) throws Exception {
        FileInputStream fis = new FileInputStream(imgUrl);
        int available = fis.available();
        byte[] bytes = new byte[available];
        fis.read(bytes);
        response.getOutputStream().write(bytes);
    }


    }

