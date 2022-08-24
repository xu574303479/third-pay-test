package com.pay.third.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用途描述:	几秒前，几分钟前，几小时前，几天前，几月前，几年前的实现
 *
 * @author xuhaibin
 * @version 1.0.0
 * @date 2017年10月3日 下午3:05:01
 */
public class DateFormatUtil {

    private final static Logger LOG = LoggerFactory.getLogger(DateFormatUtil.class);

    public static final String DATE_FORMAT_STR_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_STR_YMD = "yyyy-MM-dd";

    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_MINUTE_S = 60L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_HOUR_S = 3600L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_DAY_S = 86400L;
    private static final long ONE_WEEK = 604800000L;
    private static final long ONE_WEEK_S = 604800L;

    private static final String JUST_AGO = "刚刚";
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    private static final String ONE_MINUTE_TIMES = "分钟";
    private static final String ONE_HOUR_TIMES = "小时";
    private static final String ONE_DAY_TIMES = "天";

    public static long toSeconds(long date) {
        return date / 1000L;
    }

    public static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    public static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    public static long toDays(long date) {
        return toHours(date) / 24L;
    }

    public static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    public static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    /**
     * 获取相应秒数后时间
     *
     * @param time    格式：时间戳
     * @param seconds 秒
     * @return
     */
    public static long getEndTimestamp(long time, int seconds) {
        return seconds * ONE_SECOND + time;
    }

    /**
     * 获取相应秒数后时间
     *
     * @param time    格式：时间戳
     * @param seconds 秒
     * @return
     */
    public static long getEndTimestamp(long time, long seconds) {
        return seconds * ONE_SECOND + time;
    }

    /**
     * 日期字符串，格式转换工具类
     *
     * @param before 转换前格式
     * @param after  需要转换的格式
     * @return
     */
    public static String dataStrFormat(String time, SimpleDateFormat before, SimpleDateFormat after) throws Exception {
        Date beforeData = before.parse(time);
        String afterTime = after.format(beforeData);
        return afterTime;
    }

    /**
     * 获取昨天日期
     *
     * @return
     */
    public static String getYesterdayDate() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String yesterdayDate = dateFormat.format(calendar.getTime());
        return yesterdayDate;
    }


    /**
     * 计算两个字符串日期相差的天数
     *
     * @param end   结束日期
     * @param start 开始日期
     * @return
     */
    public static long dayDiff(String end, String start, SimpleDateFormat formater) {
        long diff = 0L;
        try {
            long d1 = formater.parse(end).getTime();
            long d2 = formater.parse(start).getTime();
            diff = (d1 - d2) / ONE_DAY;
        } catch (ParseException e) {

        }
        return diff;
    }


    /**
     * 获取相应秒数后时间
     *
     * @param timeStr 格式：yyyy-MM-dd HH:mm:ss
     * @param seconds 秒
     * @return
     */
    public static long getEndStrTimestamp(String timeStr, int seconds) {
        SimpleDateFormat sdfs = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_STR_YMDHMS);

        try {
            long time = sdfs.parse(timeStr).getTime();
            return seconds * ONE_SECOND + time;
        } catch (ParseException e) {
            return 0;
        }
    }


    /**
     * 判断日期大小，第一个是否大于（包括等于）第二个日期
     *
     * @param start 格式 2018-08-08
     * @param end   格式 2018-08-08
     * @return
     */
    public static boolean getDateOrSize(String start, String end) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long startTime = sdf.parse(start).getTime();
        long endTime = sdf.parse(end).getTime();
        if (startTime >= endTime) {
            return true;
        }
        return false;
    }

    /**
     * 比较两日期大小
     *
     * @param date1 格式 2018-08-08 08:08:08
     * @param date1 格式 2018-08-08 08:08:08
     * @return
     */
    public static int dateComparto(String date1, String date2, SimpleDateFormat sdf) throws Exception {
        long startTime = sdf.parse(date1).getTime();
        long endTime = sdf.parse(date2).getTime();
        if (startTime > endTime) {
            return 1;
        } else if (startTime == endTime) {
            return 0;
        } else {
            return -1;
        }
    }


    /**
     * 获取当前日期与结束日期相差的时间：单位-秒
     *
     * @param time   日期字符串
     * @param format 日期字符串的格式
     * @return
     */
    public static long getSeconds(String time, SimpleDateFormat format) throws Exception {
        long end = format.parse(time).getTime();
        long now = System.currentTimeMillis();
        long result = end - now;
        if (result <= 0) {
            return 0;
        }

        return toSeconds(result);
    }


    /**
     * 获取当前日期与开始日期相差的时间：单位-秒
     *
     * @param time   日期字符串
     * @param format 日期字符串的格式
     * @return
     */
    public static long getStartSecondsNow(String time, SimpleDateFormat format) throws Exception {
        long start = format.parse(time).getTime();
        long now = System.currentTimeMillis();
        long result = now - start;
        if (result <= 0) {
            return 0;
        }

        return toSeconds(result);
    }

    /**
     * 获取开始日期与当前日期相差的时间：单位-秒
     *
     * @param time   日期时间戳
     * @param format 日期字符串的格式
     * @return
     */
    public static long getSecondsStart(long time, SimpleDateFormat format) {
        long seconds = 0;
        try {
            long now = System.currentTimeMillis();
            long result = now - time;
            if (result <= 0) {
                seconds = 1;
            } else {
                seconds = toSeconds(result);
            }

        } catch (Exception e) {

        }
        return seconds;
    }


    /**
     * 获取当前日期与结束日期相差的时间：自定义格式
     *
     * @param time   日期字符串
     * @param format 日期字符串的格式
     * @return
     */
    public static String getTimeStr(String time, SimpleDateFormat format) throws Exception {
        long end = format.parse(time).getTime();
        long now = System.currentTimeMillis();
        long result = end - now;
        if (result <= 0) {
            return "0天00时01分";
        }

        long seconds = toSeconds(result);

        return formatDateTime(seconds);
    }


    /**
     * 格式化时间字符串
     *
     * @param seconds 秒数
     * @return
     */
    public static String formatDateTime(long seconds) {
        if (seconds <= 60) {
            return "0天00时01分";
        }

        String dateTimes;
        long days = seconds / (60 * 60 * 24);
        long hours = (seconds % (60 * 60 * 24)) / (60 * 60);
        long minutes = (seconds % (60 * 60)) / 60;

        String day = "0天";
        String hour = "00时";
        String min = "01分";
        if (days > 0) {
            day = days + "天";
        }
        if (hours > 0) {
            hour = getTimeStr(hours) + "时";
        }
        if (minutes > 0) {
            min = getTimeStr(minutes) + "分";
        }
        dateTimes = day + hour + min;

        return dateTimes;
    }

    /**
     * 格式化时间字符串
     *
     * @param time
     * @return
     */
    public static String getTimeStr(long time) {
        String result = "00";
        if (time <= 0) {
            result = "00";
        }
        if (time > 0 && time < 10) {
            result = "0" + time;
        }
        if (time >= 10) {
            result = String.valueOf(time);
        }
        return result;
    }

    /**
     * 日期与当前时间对比,格式换时间,返回 XX天, 小于1 天,则返回 XX分, XX时
     *
     * @param time1
     * @return
     * @throws ParseException
     */
    public static int formatDays(String time1, String time2, String formatStr) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = format.parse(time1);

        long timesmap2 = format.parse(time2).getTime();

        long delta = timesmap2 - date.getTime();

        long days = toDays(delta);

        return (int) days;
    }


    /**
     * 格式化天数,返回 XX周XX天
     *
     * @param days
     * @return
     * @throws ParseException
     */
    public static String formatDays(int days) throws Exception {
        if (days <= 0) {
            return "0天";
        } else if (days > 0 && days <= 7) {
            return days + "天";
        } else {
            int week = days / 7;
            int day = days % 7;
            return week + "周" + day + "天";
        }
    }


    /**
     * 日期与当前时间对比,格式换时间,返回 XX天, 小于1 天,则返回 XX分, XX时
     *
     * @param time1
     * @return
     * @throws ParseException
     */
    public static String formatTimesDay(String time1, String time2, String formatStr) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = format.parse(time1);

        long timesmap2 = format.parse(time2).getTime();

        long delta = timesmap2 - date.getTime();

        if (delta < 1L * ONE_MINUTE) {
            return "1" + ONE_MINUTE_TIMES;
        } else if (delta < 60L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_TIMES;
        } else if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_TIMES;
        } else {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_TIMES;
        }

    }


    /**
     * 获取XX秒前 或 XX分钟前 或 XX小时前...
     *
     * @param time 格式必须为 yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static String formatTimes(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_STR_YMDHMS);
        Date date = format.parse(time);

        long delta = System.currentTimeMillis() - date.getTime();

        if (delta < 1L * ONE_MINUTE) {
            return JUST_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    /**
     * 获取当前日期与开始日期相差的时间：单位-天
     *
     * @param time 日期字符串
     * @return
     */
    public static long getStartDaysNow(String time, SimpleDateFormat format) {

        long result = 0;
        try {
            long start = format.parse(time).getTime();
            long now = System.currentTimeMillis();
            result = now - start;
            if (result <= 0) {
                return 0;
            }

        } catch (Exception e) {

        }
        return toDays(result);
    }

    /**
     * 时间字符串截取
     *
     * @param oldFormat
     * @param newFormat
     * @param dateString
     * @return
     */
    public static String getDateSting(SimpleDateFormat oldFormat, SimpleDateFormat newFormat, String dateString) {

        try {
            if (StringUtils.isBlank(dateString)) {
                return "";
            }
            Date date = oldFormat.parse(dateString);

            String formatString = newFormat.format(date);
            return formatString;
        } catch (ParseException e) {

        }
        return "";
    }

    /**
     * 获取给定日期N天后的日期
     *
     * @param dateTime 日期字符串，格式：2019-08-08
     * @param days     天数
     * @return
     */
    public static String getDateAfterNDays(String dateTime, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(dateTime);
        long start = parse.getTime();
        long end = start + (ONE_DAY * days);
        String endTime = sdf.format(new Date(end));
        return endTime;
    }

    /**
     * 获取给定日期N天后的日期
     *
     * @param dateTime  日期字符串，格式：2019-08-08
     * @param days      天数
     * @param formatStr 日期格式,例如:yyyy-MM-dd
     * @return
     */
    public static String getAfterNDays(String dateTime, int days, String formatStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date parse = sdf.parse(dateTime);
        long start = parse.getTime();
        long end = start + (ONE_DAY * days);
        String endTime = sdf.format(new Date(end));
        return endTime;
    }

    /**
     * 获取今日日期字符串，格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getTodayDate() {
        return new SimpleDateFormat(DATE_FORMAT_STR_YMD).format(new Date());
    }

    /**
     * 获取日期字符串
     *
     * @param formatStr 日期格式,例:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowTime(String formatStr) {
        return new SimpleDateFormat(formatStr).format(new Date());
    }

    /**
     * 获取倒计时剩余时间：单位-秒
     *
     * @param orderTime
     * @param secTime   规定倒计时时间，单位：秒
     * @return
     */
    public static long getNowTimesOut(String orderTime, long secTime) {
        long seconds = 0;
        try {
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long endStrTimes = getStartSecondsNow(orderTime, sdfs);

            seconds = secTime - endStrTimes;
            if (seconds <= 0) {
                seconds = 0;
            }

        } catch (Exception e) {

        }

        return seconds;
    }
}
