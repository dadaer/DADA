package com.example.demo.service.ConnectService;

import com.example.demo.ORMEntity.Session;
import com.example.demo.ORMEntity.User;
import com.example.demo.controller.ConnectContoller.JwglController;
import com.example.demo.dao.SessionMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.*;
import com.example.demo.utils.Base64Util;
import com.example.demo.utils.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JwglService {
//    private static Logger logger = LoggerFactory.getLogger(JwglController.class);

//    @Autowired
//    private SessionMapper sessionMapper;

    @Autowired
    private UserMapper userMapper;

    private HttpSession httpSession;
    private String stuNum;
    private String stuName;
    private String __VIEWSTATE = "";
    private Map<String, String> cookies = new HashMap<>();
    private Connection connection;
    private Connection.Response response;
    private Document document;

//    public JwglService() {
//        connectIndex();
//    }

    //首次连接获取cookies和__VIEWSTATE
    public Message connectIndex(HttpSession httpSession) {
        try {
            //连接教务管理系统
            connection = Jsoup.connect("http://210.28.81.11");
            connection.header("User-Agent",// 配置模拟浏览器
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
            response = connection.timeout(9000).execute();
            //保存Cookies
            cookies = response.cookies();
            // 将响应转换为Dom树，以便获取__VIEWSTATE
            document = Jsoup.parse(response.body());
            for (Element element : document.getElementsByTag("input")) {
                if (element.attr("name").equals("__VIEWSTATE")) {
                    __VIEWSTATE = element.val();
                    break;
                }
            }
            this.httpSession = httpSession;
            Session session = new Session();
            session.setSessionId(httpSession.getId());
//            sessionMapper.insertSession(session);
            httpSession.setAttribute("cookies",cookies);
            httpSession.setAttribute("viewstate",__VIEWSTATE);
            Message message = new Message();
            return message.add("cookies",httpSession.getId());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return  null;
    }

    /**
     * 获取验证码
     * @param res
     * @param
     * @throws IOException
     */
    public void getCheckCode(HttpServletResponse res,HttpSession httpSession)
            throws IOException {
        String captcha_url = "http://210.28.81.11/CheckCode.aspx";
        response = Jsoup.connect(captcha_url)
                .cookies((Map)(httpSession.getAttribute("cookies"))).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Connection.Method.GET).timeout(9000).execute();
        byte[] bytes = response.bodyAsBytes();
        //在本地建立文件夹
          File file = new File("C:\\Users\\一号公路上的桥断了\\Desktop\\"
                  + httpSession.getId().substring(0,12) + "checkCode.png");
            if (file.exists()) {
                file.delete();
            }
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            bufferedOutput.close();
            output.close();
        System.out.println(httpSession.getId());
//        res.getOutputStream().write(bytes);
    }

    /**
     * 登录
     * @param stuNum
     * @param password
     * @param checkCode
     * @return
     */
    public Message  login(String stuNum, String password,
                          String checkCode, String stuName,HttpSession httpSession) throws Exception {
        this.stuNum = stuNum;
//        stuName = "";
        this.stuName = stuName;
        //填充post数据
        httpSession.setAttribute("stuNum",stuNum);
        httpSession.setAttribute("stuName",stuName);
        User user1 = userMapper.queryUserByStuNum(stuNum);
        if (user1 == null) {
            User user = new User();
            user.setStuNum(stuNum);
            user.setPassword(Base64Util.encode(password));
            user.setStuName(stuName);
            userMapper.addUser(user);
        }
        Map<String, String> datas = new HashMap<>();
        datas.put("__VIEWSTATE", (String) httpSession.getAttribute("viewstate"));
        datas.put("txtUserName", stuNum);
        datas.put("TextBox2", password);
        datas.put("txtSecretCode", checkCode);
        datas.put("RadioButtonList1", "%D1%A7%C9%FA");
        datas.put("Button1", "");
        datas.put("lbLanguage", "");
        datas.put("hidPdrs", "");
        datas.put("hidsc", "");
        Message message = new Message();
        try {
            connection = Jsoup.connect("http://210.28.81.11/default2.aspx");
            connection.header("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
            // 设置cookie和post上面的map数据
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.POST)
                    .data(datas).cookies((Map)(httpSession.getAttribute("cookies"))).execute();
            document = response.parse();

            if (document.title().equals("欢迎使用正方教务管理系统！请登录")) {
                String error = document.getElementsByTag("script").get(1).data();
                Pattern pattern = Pattern.compile("(?<=alert\\(')[^']*");
                Matcher matcher = pattern.matcher(error);
                while (matcher.find()) {
                    System.out.print("登录失败,");
                    System.out.println(matcher.group());
                }
                return message.fail();
            } else {
//                try {
//                    stuName = document.select(".info [id=xhxm]").text();
//                    stuName = stuName.substring(0, stuName.length() - 2);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
                System.out.println("登录成功，欢迎你" + stuName + "!");
                return message.success();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return message.fail();
        }
    }

    /**
     * 获取姓名
     * @return
     */
    public String getInfo(){
        return stuName;
    }

    /**
     * 获取课表
     * @return
     * @throws IOException
     */
    public List<Course> getCourse(HttpSession httpSession) throws IOException {
        String __VIEWSTATE1 = null;
        try {
            String CourseURL = "http://210.28.81.11/xsxkqk.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121615";
            connection = Jsoup.connect(CourseURL);
            response = connection.ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + httpSession.getAttribute("stuNum"))
                    .cookies((Map)(httpSession.getAttribute("cookies"))).postDataCharset("GB2312").timeout(9000).execute();
            document = response.parse();
            for (Element element : document.getElementsByTag("input")) {
                if (element.attr("name").equals("__VIEWSTATE")) {
                    __VIEWSTATE1 = element.val();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> datas = new HashMap<>();
        datas.put("__VIEWSTATE", __VIEWSTATE1);
        datas.put("ddlXN","2018-2019");
        datas.put("ddlXQ", "2");
        datas.put("__EVENTTARGET", "ddlXQ");
        datas.put("__EVENTARGUMENT", "");
        String CourseURL = "http://210.28.81.11/xsxkqk.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121615";
        connection = Jsoup.connect(CourseURL);
        response = connection.ignoreContentType(true).method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                .data(datas).cookies((Map)(httpSession.getAttribute("cookies"))).postDataCharset("GB2312").timeout(9000).execute();
        document = response.parse();
        Elements elements = document.select(".datelist tr");
        List<Course> list = new LinkedList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                if (elements.get(i).text().contains("重修") || elements.get(i).text().contains("+")) {
                    continue;
                }
                if (elements.get(i).text().contains(";")) {
                    int x;
                    if (elements.get(i).text().split(" ").length == 15) {
                        x = elements.get(i).text().split(" ")[8].split(";").length;
                    } else {
                        x = elements.get(i).text().split(" ")[9].split(";").length;
                    }
                    for (int a = 0; a < x; a++) {
                        Course course = new Course();
                        course.setCourseName(elements.get(i).text().split(" ")[2]);
                        System.out.println(course.getCourseName());
                        course.setCourseTeacher(elements.get(i).text().split(" ")[5]);
                        if (elements.get(i).text().split(" ").length == 15) {
                            course.setCourseTime(elements.get(i).text().split(" ")[8].split(";")[a]);
                        } else {
                            course.setCourseTime(elements.get(i).text().split(" ")[9].split(";")[a]);
                        }
//                        course.setCourseTime(elements.get(i).text().split(" ")[8].split(";")[a]);
                        System.out.println(course.getCourseTime());
                        if(course.getCourseTime().length() == 0 || course.getCourseTime().substring(0,1) == null) {
                            continue;
                        }
                        int m = course.getCourseTime().indexOf("{");
                        int n = course.getCourseTime().indexOf("}");
                        String str9 = course.getCourseTime().substring(m + 1, n);
                        String str6;
                        String str7;
                        String str8;
                        int length;
                        int courseDay = 0;
                        str6 = course.getCourseTime().substring(0, 2);
                        switch (str6) {
                            case "周一":
                                courseDay = 1;
                                break;
                            case "周二":
                                courseDay = 2;
                                break;
                            case "周三":
                                courseDay = 3;
                                break;
                            case "周四":
                                courseDay = 4;
                                break;
                            case "周五":
                                courseDay = 5;
                                break;
                            case "周六":
                                courseDay = 6;
                                break;
                            case "周日":
                                courseDay = 7;
                                break;
                        }
                        if (course.getCourseTime().contains("10") ) {
                            int s = course.getCourseTime().indexOf("10");
                            int q = course.getCourseTime().indexOf("第");
                            int w = course.getCourseTime().indexOf("节");
                            if ( s > w) {
                                continue;
                            }
                            str7 = course.getCourseTime().substring(q + 1, q + 3);
                            str8 = course.getCourseTime().substring(w - 2, w);
                            length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                            course.setCourseStart(str7);
                            course.setCourseFinish(str8);
                            course.setCourseLength(length);
                        } else if (course.getCourseTime().contains("12")
                                && !course.getCourseTime().contains("10")) {
//                            int q = course.getCourseTime().indexOf("第");
//                            int w = course.getCourseTime().indexOf("节");
//                            str7 = course.getCourseTime().substring(q + 1, q + 2);
//                            str8 = course.getCourseTime().substring(w - 2, w);
//                            length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                            course.setCourseStart("12");
                            course.setCourseFinish("12");
                            course.setCourseLength(1);
                        } else {
                            int q = course.getCourseTime().indexOf("第");
                            int w = course.getCourseTime().indexOf("节");
                            str7 = course.getCourseTime().substring(q + 1, q + 2);
                            str8 = course.getCourseTime().substring(w - 1, w);
                            length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                            course.setCourseStart(str7);
                            course.setCourseFinish(str8);
                            course.setCourseLength(length);
                        }
                        course.setCourseDay(courseDay);
                        course.setCourseWeek(str9);
                        if (elements.get(i).text().split(" ")[9].length() > 1) {
                            course.setCourseArea(elements.get(i).text().split(" ")[9].split(";")[a]);
                        }
//                        courses[++j] = course;
                        list.add(course);
                    }
                } else {
                    Course course = new Course();
//                    System.out.println(elements.get(i).text().split(" ").length);
                    course.setCourseName(elements.get(i).text().split(" ")[2]);
                    System.out.println(course.getCourseName());
                    course.setCourseTeacher(elements.get(i).text().split(" ")[5]);
                    if (elements.get(i).text().split(" ").length == 15) {
                        course.setCourseTime(elements.get(i).text().split(" ")[8]);
                    } else {
                        course.setCourseTime(elements.get(i).text().split(" ")[9]);
                    }
                    System.out.println(course.getCourseTime());
                    if(course.getCourseTime().length() == 0 || course.getCourseTime().substring(0,1) == null) {
                        continue;
                    }
                    int m = course.getCourseTime().indexOf("{");
                    int n = course.getCourseTime().indexOf("}");
                    String str9 = course.getCourseTime().substring(m + 1, n);
                    String str6;
                    String str7;
                    String str8;
                    int length;
                    int courseDay = 0;
                    str6 = course.getCourseTime().substring(0, 2);
                    switch (str6) {
                        case "周一":
                            courseDay = 1;
                            break;
                        case "周二":
                            courseDay = 2;
                            break;
                        case "周三":
                            courseDay = 3;
                            break;
                        case "周四":
                            courseDay = 4;
                            break;
                        case "周五":
                            courseDay = 5;
                            break;
                        case "周六":
                            courseDay = 6;
                            break;
                        case "周日":
                            courseDay = 7;
                            break;
                    }
                    if (course.getCourseTime().contains("10")) {
                        int s = course.getCourseTime().indexOf("10");
                        int q = course.getCourseTime().indexOf("第");
                        int w = course.getCourseTime().indexOf("节");
                        if ( s > w) {
                            continue;
                        }
                        str7 = course.getCourseTime().substring(q + 1, q + 3);
                        str8 = course.getCourseTime().substring(w - 2, w);
                        length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                        course.setCourseStart(str7);
                        course.setCourseFinish(str8);
                        course.setCourseLength(length);
                    } else if (course.getCourseTime().contains("12")
                            && !course.getCourseTime().contains("10")) {
//                            int q = course.getCourseTime().indexOf("第");
//                            int w = course.getCourseTime().indexOf("节");
//                            str7 = course.getCourseTime().substring(q + 1, q + 2);
//                            str8 = course.getCourseTime().substring(w - 2, w);
//                            length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                        course.setCourseStart("12");
                        course.setCourseFinish("12");
                        course.setCourseLength(1);
                    } else {
                        int q = course.getCourseTime().indexOf("第");
                        int w = course.getCourseTime().indexOf("节");
                        str7 = course.getCourseTime().substring(q + 1, q + 2);
                        str8 = course.getCourseTime().substring(w - 1, w);
                        length = Integer.parseInt(str8) - Integer.parseInt(str7) + 1;
                        course.setCourseStart(str7);
                        course.setCourseFinish(str8);
                        course.setCourseLength(length);
                    }
                    course.setCourseDay(courseDay);
                    course.setCourseWeek(str9);
                    course.setCourseArea(elements.get(i).text().split(" ")[9]);
                    list.add(course);
                }
            }
        }
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            Course coursei = (Course) iterator.next();
            String weeki = coursei.getCourseWeek().trim();
            List<Integer> list1 = new ArrayList<>();
            int index1 = weeki.indexOf("第");
            int index2 = weeki.indexOf("周");
            int index3 = weeki.indexOf("-");
            String startWeek = weeki.substring(index1+1,index3);
            String endWeek = weeki.substring(index3+1,index2);
            int startWeek1 = Integer.parseInt(startWeek);
            int endWeek1 = Integer.parseInt(endWeek);
            for (int i = startWeek1 ; i <= endWeek1 ; i++){
                if(weeki.contains("单周")){
                    if(i % 2 == 1){
                        list1.add(i);
                    }
                }else if(weeki.contains("双周")){
                    if(i % 2 == 0){
                        list1.add(i);
                    }
                }else {
                    list1.add(i);
                }
            }
            coursei.setWeeks(list1);
        }
        Collections.sort(list, new Comparator<Course>() {
            @Override
            public int compare(Course a, Course b) {
                return Integer.parseInt(a.getCourseFinish()) - Integer.parseInt(b.getCourseFinish());
            }
        });
        return list;
    }

    /**
     * 获取学生成绩
     * @return
     * @throws IOException
     */
    public List<Grade> getGrade(HttpSession httpSession) throws IOException {
        String __VIEWSTATE1 = null;
        try {
            String CourseURL = "http://210.28.81.11/xscj.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121605";
            connection = Jsoup.connect(CourseURL);
            response = connection.ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                    .cookies((Map)(httpSession.getAttribute("cookies"))).postDataCharset("GB2312").timeout(9000).execute();
            document = response.parse();
            for (Element element : document.getElementsByTag("input")) {
                if (element.attr("name").equals("__VIEWSTATE")) {
                    __VIEWSTATE1 = element.val();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> datas = new HashMap<>();
        datas.put("__VIEWSTATE", __VIEWSTATE1);
        datas.put("ddlXN","2018-2019");
        datas.put("ddlXQ", "1");
        datas.put("txtQSCJ", "0");
        datas.put("txtZZCJ", "100");
        datas.put("Button1", "%b0%b4%d1%a7%c6%da%b2%e9%d1%af");
        try {
            String GradeURL = "http://210.28.81.11/xscj.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121605";
            connection = Jsoup.connect(GradeURL);
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xscj.aspx?xh=" + stuNum + "&xm=" + URLEncoder.encode(stuName, "GB2312") + "&gnmkdm=N121605")
                    .data(datas).cookies((Map)(httpSession.getAttribute("cookies"))).execute();
            document = response.parse();
            Elements elements = document.select("table.datelist tr");
            List<Grade> list = new LinkedList<>();
            for (int i = 1; i < elements.size() - 1; i++) {
                if(elements.get(i).select("td").get(3).text().contains("最高成绩值")) {
                    break;
                }
                if (i > 0 && i < elements.size() - 1) {
                    Grade grade = new Grade();
                    grade.setCourseName(elements.get(i).select("td").get(1).text());
                    grade.setCourseGrade(elements.get(i).select("td").get(3).text());
                    list.add(grade);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取考试安排
     * @return
     */
    public List<Exam> getExam(HttpSession httpSession) {
        String __VIEWSTATE1 = null;
        try {
            String CourseURL = "http://210.28.81.11/xskscx.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121604";
            connection = Jsoup.connect(CourseURL);
            response = connection.ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                    .cookies((Map)(httpSession.getAttribute("cookies"))).postDataCharset("GB2312").timeout(9000).execute();
            document = response.parse();
            for (Element element : document.getElementsByTag("input")) {
                if (element.attr("name").equals("__VIEWSTATE")) {
                    __VIEWSTATE1 = element.val();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map datas = new HashMap();
        datas.put("__EVENTTARGET", "xqd");
        datas.put("__EVENTARGUMENT", "");
        datas.put("__VIEWSTATE", __VIEWSTATE1);
        datas.put("xnd", "2018-2019");
        datas.put("xqd", "1");
        try {
            String CourseURL = "http://210.28.81.11/xskscx.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121604";
            connection = Jsoup.connect(CourseURL);
            response = connection.ignoreContentType(true).method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                    .cookies((Map)(httpSession.getAttribute("cookies"))).data(datas).postDataCharset("GB2312").timeout(9000).execute();
            document = response.parse();
            System.out.println("考试安排");
            Elements elements = document.select("table.datelist tr");
            List<Exam> list = new LinkedList<>();
            for (int i = 1; i < elements.size(); i++) {
                if (i > 0 && i < elements.size()) {
                    Exam exam = new Exam();
                    exam.setExamName(elements.get(i).select("td").get(1).text());
                    exam.setDate(elements.get(i).select("td").get(3).text());
                    exam.setExamPlace(elements.get(i).select("td").get(4).text());
                    exam.setExamNumber(elements.get(i).select("td").get(6).text());
                    list.add(exam);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取等级考试
     * @return
     */
    public List<CET> getCET(HttpSession httpSession) {

        try {
            String CETURL = "http://210.28.81.11/xsdjkscx.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121606";
            connection = Jsoup.connect(CETURL);
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                    .cookies((Map)(httpSession.getAttribute("cookies"))).execute();
            document = response.parse();
//            System.out.println(document);
            Elements elements = document.select(".datelist tr");
            List<CET> list = new LinkedList<>();
            for (int i = 0; i < elements.size(); i++) {
                CET cet = new CET();
                if (i > 0) {
                    cet.setYear(elements.get(i).text().split(" ")[0]);
                    cet.setTerm(elements.get(i).text().split(" ")[1]);
                    cet.setName(elements.get(i).text().split(" ")[2]);
                    cet.setScore(elements.get(i).text().split(" ")[5]);
                    list.add(cet);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取空教室
     * @return
     */
    public String[] getClassroom(HttpSession httpSession) {
        String __VIEWSTATE2 = null;
        try {
            String VIEWURL = "http://210.28.81.11/xxjsjy.aspx?xh=" + httpSession.getAttribute("stuNum") + "&xm="
                    + URLEncoder.encode((String) httpSession.getAttribute("stuName"), "GB2312") + "&gnmkdm=N121611";
            connection = Jsoup.connect(VIEWURL);
            response = connection.ignoreContentType(true).method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xs_main.aspx?xh=" + stuNum)
                    .cookies((Map)(httpSession.getAttribute("cookies"))).postDataCharset("GB2312").timeout(9000).execute();
            document = response.parse();
            for (Element element : document.getElementsByTag("input")) {
                if (element.attr("name").equals("__VIEWSTATE")) {
                    __VIEWSTATE2 = element.val();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> datas = new HashMap<>();
        datas.put("__VIEWSTATE", __VIEWSTATE2);
        datas.put("__EVENTTARGET", "");
        datas.put("__EVENTARGUMENT", "");
        datas.put("xiaoq", "");
        datas.put("jslb", "0");
        datas.put("min_zws", "0");
        datas.put("max_zws", "");
        datas.put("ddlKsz", String.valueOf(DateUtils.getWeek()));//开始周
        System.out.println(DateUtils.getWeek());
        datas.put("ddlJsz", String.valueOf(DateUtils.getWeek()));//结束周
        datas.put("xqj", String.valueOf(DateUtils.getWeekDay()));//星期几
        System.out.println(DateUtils.getWeekDay());
        datas.put("ddlDsz", "˫");
        datas.put("sjd", DateUtils.getNowCourse());//第几节课
        System.out.println(DateUtils.getNowCourse());
        datas.put("Button2", "%bf%d5%bd%cc%ca%d2%b2%e9%d1%af");
        datas.put("xn", "2018-2019");
        datas.put("xq", "2");
        datas.put("ddlSyXn", "2018-2019");
        datas.put("ddlSyxq", "2");
        try {
            String ClassroomURL = "http://210.28.81.11/xxjsjy.aspx?xh=" + stuNum + "&xm=" + URLEncoder.encode(stuName, "GB2312") + "&gnmkdm=N121611";
            connection = Jsoup.connect(ClassroomURL);
            response = connection.postDataCharset("GB2312").ignoreContentType(true).method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
                    .referrer("http://210.28.81.11/xxjsjy.aspx?xh=" + stuNum + "&xm=" + "%BD%AA%B4%F3%C0%CA" + "&gnmkdm=N121611")
                    .data(datas).cookies(cookies).execute();
            document = response.parse();
            Elements elements = document.select("table.datelist tr");
            String[] rooms = new String[elements.size()];
            for (int i = 0; i < elements.size(); i++) {
                if (i > 1) {
                    rooms[i] = elements.get(i).text().split(" ")[1];
                }
            }
            return rooms;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
