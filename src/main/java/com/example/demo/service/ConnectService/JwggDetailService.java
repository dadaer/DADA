package com.example.demo.service.ConnectService;

import com.example.demo.entity.NewsDetail;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwggDetailService {
    private Connection connection;
    private Connection.Response response;
    private Document document;
    private Map<String,String> cookies = new HashMap<>();

    public NewsDetail getJwDetail(String src) throws IOException {
        System.out.println(src);
        NewsDetail newsDetail = new NewsDetail();
        String article[];
        String title;
        connection =  Jsoup.connect(src);
        //connection =  Jsoup.connect("http://xinwen.nufe.edu.cn/ncyw/index_2.jhtml");
        connection.header("User-Agent",// 配置模拟浏览器
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
//            connection.header("User-Agent",// 配置模拟浏览器
//                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        response = connection.timeout(9000).execute();
        //response = connection.timeout(9000).execute();
        //保存Cookies
        cookies = response.cookies();
        document = Jsoup.parse(response.body());
        Elements elements = document.select("#article p");
        article = elements.text().split(" ");
        String[] para = new String[article.length ];
        title = article[0];
        for(int  i = 0;i < article.length;i++) {
            para[i] = article[i];
            newsDetail.setPara(para);
        }
        newsDetail.setTitle(title);
        return newsDetail;
    }
}
