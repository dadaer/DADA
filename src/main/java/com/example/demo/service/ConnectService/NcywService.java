package com.example.demo.service.ConnectService;

import com.example.demo.entity.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NcywService {
    private Connection connection;
    private Connection.Response response;
    private Element elements;
    private Document document;
    private Map<String, String> cookies = new HashMap<>();

    public List<News> getNcyw() {
        try {
            //连接教务管理系统

            connection = Jsoup.connect("http://www.njue.edu.cn/tglm/ncyw.htm");
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
            Elements elements = document.select(".list li");
            int length = elements.size();
            List<News> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                News news = new News();
                news.setTitle(elements.get(i).select(".list-txt-1 h3").text());
                news.setDate(elements.get(i).select(".list-date").text());
                news.setSrc("www.njue.edu.cn" + elements.get(i).select("a").attr("href").substring(2));
                list.add(news);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
