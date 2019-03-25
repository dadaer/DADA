package com.example.demo.service.ConnectService;

import com.example.demo.entity.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwggService {
    private Connection connection;
    private Connection.Response response;
    private Document document;
    private Map<String, String> cookies = new HashMap<>();

    public List<News> getJwgg() {
        try {
            connection = Jsoup.connect("http://jwc.nufe.edu.cn/index/tzgg1.htm");
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
            Elements elements = document.select("#article_lst table");
            Elements elements1 = elements.select("tr");
            List<News> news1 = new ArrayList<>();
            String newsTitle;
            String newsDate;
            String newsi;
            String src;
            for (int i = 0; i < elements1.size(); i++) {
                News newsii = new News();
                newsi = elements1.get(i).text();
                src = elements1.get(i).select("a").attr("href").substring(2);
                newsTitle = newsi.split(" ")[0];
                newsDate = newsi.split(" ")[1];
                newsii.setSrc("http://jwc.nufe.edu.cn" + src);
                newsii.setTitle(newsTitle);
                newsii.setDate(newsDate);
                news1.add(newsii);
            }
            return news1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
