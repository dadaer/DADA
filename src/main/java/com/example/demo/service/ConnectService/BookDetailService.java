package com.example.demo.service.ConnectService;

import com.example.demo.libEntity.Detail;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class BookDetailService {
    private Connection connection;
    private Connection.Response response;
    private Document document;

    public Detail[] getBookDetail(String search_book) throws UnsupportedEncodingException {
        connection = Jsoup.connect("http://opac.nufe.edu.cn/opac/openlink.php?strText=" + search_book + "&strSearchType=title&");
        connection.header("User-Agent",// 配置模拟浏览器
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        try {
            response = connection.execute();
            document = response.parse();
            Elements elements = document.select(".book_list_info");
            int length = elements.size();
            Detail[] details = new Detail[length];
            for (int i = 0; i < length; i++) {
                Detail detail = new Detail();
                detail.setBookName(elements.get(i).select("a").text());
                int length1 = elements.get(i).select("p").text().split(" ").length;
                detail.setGcfb(elements.get(i).select("p").text().split(" ")[0]);
                detail.setKjfb(elements.get(i).select("p").text().split(" ")[1]);
                detail.setAuthor(elements.get(i).select("p").text().split(" ")[2]);
                detail.setCbs(elements.get(i).select("p").text().split(" ")[length1 - 3]);
                details[i] = detail;
            }
            return details;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
