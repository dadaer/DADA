package com.example.demo.service.ConnectService;

import com.example.demo.entity.NewsDetail;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class   NcywDetailService {
    private Connection connection;
    private Connection.Response response;
    private Element elements;
    private Document document;
    private Map<String,String> cookies = new HashMap<>();

    public NewsDetail getYwDetail(String src){
        try{
            //连接教务管理系统

            connection =  Jsoup.connect("http://" +src);
            //connection =  Jsoup.connect("http://xinwen.nufe.edu.cn/ncyw/index_2.jhtml");
            connection.header("User-Agent",// 配置模拟浏览器
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
//            connection.header("User-Agent",// 配置模拟浏览器
//                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
            response = connection.timeout(9000).execute();
            //response = connection.timeout(9000).execute();
            //保存Cookies
            cookies = response.cookies();
            // 将响应转换为Dom树，以便获取__VIEWSTATE
            document = Jsoup.parse(response.body());
            Elements element = document.select(".content");
            NewsDetail newsDetail = new NewsDetail();
            newsDetail.setTitle(element.select(".content-title h3").text());
            Elements elements = element.select(".content-con p");
            int length = elements.size();
            String[] article = new String[elements.size()];
            for(int i = 0 ;i < length; i ++){
                article[i] = elements.get(i).text();
            }
            newsDetail.setPara(article);
            return newsDetail;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
