package com.example.demo.service.ConnectService;

import com.example.demo.dao.SessionMapper;
import com.example.demo.libEntity.Current;
import com.example.demo.libEntity.History;
import com.example.demo.libEntity.Number;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class LibService {
    @Autowired
    private SessionMapper sessionMapper;

    private Connection connection;
    private Connection.Response response;
    private Document document;
    private String token;
    private Map<String,String> cookies = new HashMap<>();

//    public LibService(){
//        connectIndex();
//    }


    public void connectIndex(HttpSession httpSession){
        try{
            connection = Jsoup.connect("http://opac.nufe.edu.cn/reader/login.php");
            response = connection.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0").execute();
            cookies = response.cookies();
            document = response.parse();
            token = document.select("[name=csrf_token]").val();
//            Session session = new Session();
//            session.setSessionId(httpSession.getId());
//            sessionMapper.insertSession(session);
            httpSession.setAttribute("libCookies",cookies);
            httpSession.setAttribute("token",token);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取验证码
     * @param res
     * @param httpSession
     * @throws IOException
     */
    public void getCaptcha(HttpServletResponse res, HttpSession httpSession)
            throws IOException {
        System.out.println(httpSession.getAttribute("libCookies"));
        String captcha_url = "http://opac.nufe.edu.cn/reader/captcha.php";
        response = Jsoup.connect(captcha_url)
                .cookies((Map) httpSession.getAttribute("libCookies")).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Connection.Method.GET).timeout(9000).execute();
        byte[] bytes = response.bodyAsBytes();
        //在本地建立文件夹
          File file = new File("/home/captcha"+
                  httpSession.getId().substring(0,12) + "captcha.png");
            if (file.exists()) {
                file.delete();
            }
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            bufferedOutput.close();
            output.close();
//        res.getOutputStream().write(bytes);
    }


    /**
     * 登录且获得数字信息
     * @param stuNum
     * @param password
     * @return
     * @throws IOException
     */
    public Number getNumber(String stuNum, String password, String captcha,HttpSession httpSession) throws IOException {
        Number number = new Number();
        Map<String,String> datas = new HashMap<>();
        datas.put("number",stuNum);
        datas.put("passwd",password);
        datas.put("captcha",captcha);
        datas.put("select","cert_no");
        datas.put("csrf_token",(String) httpSession.getAttribute("token"));
        datas.put("returnUrl","");
        try {
            connection = Jsoup.connect("http://opac.nufe.edu.cn/reader/redr_verify.php");
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .data(datas).cookies((Map)httpSession.getAttribute("libCookies")).execute();
            document = Jsoup.parse(response.body());
            Elements elements = document.select(".bigger-170");
            number.setZdkj(elements.text().split(" ")[0]);
                    number.setZdyy(elements.text().split(" ")[1]);
            number.setZdwt(elements.text().split(" ")[2]);
            return number;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取历史借阅
     * @return
     */
    public List<History> getHistory(HttpSession httpSession){
        try{
            connection = Jsoup.connect("http://opac.nufe.edu.cn/reader/book_hist.php");
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .cookies((Map)httpSession.getAttribute("libCookies")).execute();
            document = Jsoup.parse(response.body());
            Elements elements = document.select(".table_line tr");
//            History[] histories = new History[elements.size()];
            List<History> list= new LinkedList<>();
            for(int i = 0;i < elements.size(); i++){
                if(i > 0){
                    History history = new History();
                    if(elements.get(i).text().split(" ").length == 7) {
                        history.setName(elements.get(i).text().split(" ")[2]);
                        history.setAuthor(elements.get(i).text().split(" ")[3]);
                        history.setJyrq(elements.get(i).text().split(" ")[4]);
                        history.setGhrq(elements.get(i).text().split(" ")[5]);
//                        histories[i] = history;
                        list.add(history);
                    }else if(elements.get(i).text().split(" ").length == 8){
                        history.setName(elements.get(i).text().split(" ")[2]);
                        history.setAuthor(elements.get(i).text().split(" ")[3]
                                + elements.get(i).text().split(" ")[4]);
                        history.setJyrq(elements.get(i).text().split(" ")[5]);
                        history.setGhrq(elements.get(i).text().split(" ")[6]);
//                        histories[i] = history;
                        list.add(history);
                    }else {
                        history.setName(elements.get(i).text().split(" ")[2]);
                        history.setAuthor(elements.get(i).text().split(" ")[4]
                                + elements.get(i).text().split(" ")[5]);
                        history.setJyrq(elements.get(i).text().split(" ")[6]);
                        history.setGhrq(elements.get(i).text().split(" ")[7]);
//                        histories[i] = history;
                        list.add(history);
                    }
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前借阅
     * @return
     */
    public List<Current> getCurrent(HttpSession httpSession){
        try{
            connection = Jsoup.connect("http://opac.nufe.edu.cn/reader/book_lst.php");
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .cookies((Map)httpSession.getAttribute("libCookies")).execute();
            document = Jsoup.parse(response.body());
            Elements elements = document.select(".table_line tr");
//            History[] histories = new History[elements.size()];
            List<Current> list = new LinkedList<>();
            for(int i = 0;i < elements.size(); i++){
                if(i > 0){
                    Current current = new Current();
                    current.setName(elements.get(i).text().split(" ")[1]);
                    current.setJyrq(elements.get(i).text().split(" ")[4]);
                    current.setYhrq(elements.get(i).text().split(" ")[5]);
                    list.add(current);
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
