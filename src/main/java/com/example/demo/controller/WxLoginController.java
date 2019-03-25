package com.example.demo.controller;


import com.example.demo.entity.Message;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.http.HttpClient;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WxLoginController {
    private Connection connection;
    private Connection.Response response;

    @RequestMapping("/wxLogin")
    public Message Wxlogin(String code) throws IOException {
        System.out.println(code);
//        GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID
//        &secret=SECRET
//        &js_code=JSCODE
//        &grant_type=authorization_code
    connection = Jsoup.connect("https://api.weixin.qq.com/sns/jscode2session");
    Map<String,String> datas = new HashMap<>();
    datas.put("appid","wx00a7be6760c86984");
    datas.put("secret","2474e6ab77b741728940e61a64e41f1f");
    datas.put("js_code",code);
    datas.put("grant_type","authorization_code");
    connection.header("User-Agent",// 配置模拟浏览器
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
            .data(datas);

    response = connection.execute();
    String result = response.body();
    System.out.println(result);
    return Message.success().add("result",result);
    }
}
