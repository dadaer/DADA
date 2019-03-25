package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static String startDay = GlobalConstant.START_DAY;// 开学日期
    private static String endDay = GlobalConstant.END_DAY;// 放假日期

    public static String getNowCourse() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
    String nowDate = df.format(new Date());
        if (DateUtils.isInTime("20:20-22:00",nowDate) ) {
            return GlobalConstant.CLASS7;
        }else if (DateUtils.isInTime("18:30-20:10",nowDate) ) {
            return GlobalConstant.CLASS6;
        }else if (DateUtils.isInTime("15:50-18:29",nowDate) ) {
            return GlobalConstant.CLASS5;
        }else if (DateUtils.isInTime("13:55-15:49",nowDate) ) {
            return GlobalConstant.CLASS4;
        }else if (DateUtils.isInTime("13:00-13:54",nowDate) ) {
            return GlobalConstant.CLASS3;
        }else if (DateUtils.isInTime("10:15-12:59",nowDate) ) {
        return GlobalConstant.CLASS2;// 34节课，以此类推。
        } else {
            return GlobalConstant.CLASS1;
        }
}
    /**
     * 获取当前时间是否在某个时间段内
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            }
            else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }
    /**
     * 获取当前时间是第几周
     *
     * @return
     */
    public static int getWeek() {
        int days = 0;
        int nowWeek = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
            String nowDate = df.format(new Date());
            int nowDaysBetween = daysBetween(startDay, nowDate) + 1;
            days = daysBetween(startDay, endDay);
            int x = nowDaysBetween % 7;
            if (x == 0) {
                nowWeek = nowDaysBetween / 7;
            } else {
                nowWeek = nowDaysBetween / 7 + 1;
            }

        } catch (ParseException e) {
            System.out.println("输入的日期不合法，解析日期失败");
            e.printStackTrace();
        }
        return nowWeek;
    }

    /**
     * 获取当前时间是星期几
     *
     * @return
     */
    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (cal.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
            return 7;
        }
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 计算两个String类型日期之间的天数
     *
     * @param startDay
     * @param endDay
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String startDay, String endDay)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDay));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDay));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 以yyyy-MM-dd HH:mm:ss格式返回String类型系统时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(new Date());
    }
}
