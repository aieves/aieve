package ai.eve.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * 日期相关的工具类
 * </p>
 *
 * @author hanym
 * @version 2015-9-21 下午1:03:06
 * @Copyright 2015 EVE. All rights reserved.
 */


public class EDate {


    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间日期格式化到年月日.
     */
    public static String dateFormatYMD = "yyyy-MM-dd";

    /**
     * 时间日期格式化到年月.
     */
    public static String dateFormatYM = "yyyy-MM";

    /**
     * 时间日期格式化到年月日时分.
     */
    public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 时间日期格式化到月日.
     */
    public static String dateFormatMD = "MM/dd";

    /**
     * 时分秒.
     */
    public static String dateFormatHMS = "HH:mm:ss";

    /**
     * 时分.
     */
    public static String dateFormatHM = "HH:mm";
    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String类型的当前日期时间
     */
    public static String GetCurrentDate(String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return curDateTime;

    }

    /**
     * 描述：获取指定日期时间的字符串(可偏移).
     *
     * @param strDate       String形式的日期时间
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值，
     *                      如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String GetStringByOffset(String strDate, String format,
                                           int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
        	ELog.E(e.getMessage());
        }
        return mDateTime;
    }

    /**
     * 两个日期相差的时间，精确到秒
     *
     * @param date1  日期1
     * @param date2  日期2
     * @param format 日期格式
     * @return String类型的时间值
     */
    public static String GetGapTimes(String date1, String date2, String format) {
        if (date1 == null || date1.equals(""))
            return "0";
        if (date2 == null || date2.equals(""))
            return "0";
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        long hour = (date.getTime() - mydate.getTime()) / (60 * 60 * 1000)
                - (day) * 24;
        long minute = (date.getTime() - mydate.getTime()) / (60 * 1000)
                - (hour) * 60 - (day) * 24 * 60;
        long second = (date.getTime() - mydate.getTime()) / (1000) - (hour)
                * 60 * 60 - (day) * 24 * 60 * 60 - minute * 60;
        String dateStr = day + "天" + hour + "小时" + minute + "分钟" + second + "秒";
        return dateStr;
    }


    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的天数
     */
    public static int GetOffectDay(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int GetOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = GetOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int GetOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = GetOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date GetDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
        	ELog.E(e.getMessage());
        }
        return date;
    }

    /**
     * 描述：获取偏移之后的Date.
     *
     * @param date          日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public static Date GetDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return c.getTime();
    }

    /**
     * 描述：Date类型转化为String类型(可偏移).
     *
     * @param date          the date
     * @param format        the format
     * @param calendarField the calendar field
     * @param offset        the offset
     * @return String String类型日期时间
     */
    public static String Date2StringByFormat(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return strDate;
    }


    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String GetStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return strDate;
    }
    /**
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String GetCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return mDateTime;

    }


    /**
     * 描述：获取本周一.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String GetFirstDayOfWeek(String format) {
        return GetDayOfWeek(format, Calendar.MONDAY);
    }

    /**
     * 描述：获取本周日.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String GetLastDayOfWeek(String format) {
        return GetDayOfWeek(format, Calendar.SUNDAY);
    }

    /**
     * 描述：获取本周的某一天.
     *
     * @param format        the format
     * @param calendarField the calendar field
     * @return String String类型日期时间
     */
    public static String GetDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return strDate;
    }

    /**
     * 描述：获取本月第一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String GetFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            //当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return strDate;

    }

    /**
     * 描述：获取本月最后一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String GetLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return strDate;
    }


    /**
     * 描述：获取表示当前日期的0点时间毫秒数.
     *
     * @return the first time of day
     */
    public static long GetFirstTimeOfDay() {
        Date date = null;
        try {
            String currentDate = GetCurrentDate(dateFormatYMD);
            date = GetDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return -1;
    }

    /**
     * 描述：获取表示当前日期24点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long GetLastTimeOfDay() {
        Date date = null;
        try {
            String currentDate = GetCurrentDate(dateFormatYMD);
            date = GetDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }
        return -1;
    }

    /**
     * 描述：判断是否是闰年()
     * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean IsLeapYear(int year) {
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 描述：根据时间返回格式化后的时间的描述.
     * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     *
     * @param strDate   the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String FormatDateStr2Desc(String strDate, String outFormat) {

        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = GetOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = GetOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    Date date = GetDateByFormat(strDate, outFormat);
                    return "今天" + GetStringByFormat(date, dateFormatHM);
                    //return h + "小时前";
                } else if (h < 0) {
                    //return Math.abs(h) + "小时后";
                } else if (h == 0) {
                    int m = GetOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    } else if (m < 0) {
                        //return Math.abs(m) + "分钟后";
                    } else {
                        return "刚刚";
                    }
                }

            } else if (d > 0) {
                if (d == 1) {
                    //return "昨天"+getStringByFormat(strDate,outFormat);
                } else if (d == 2) {
                    //return "前天"+getStringByFormat(strDate,outFormat);
                }
            } else if (d < 0) {
                if (d == -1) {
                    //return "明天"+getStringByFormat(strDate,outFormat);
                } else if (d == -2) {
                    //return "后天"+getStringByFormat(strDate,outFormat);
                } else {
                    //return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
                }
            }
            Date date = GetDateByFormat(strDate, outFormat);
            String out = GetStringByFormat(date, outFormat);
            if (!"".equals("") && out != null) {
                return out;
            }
        } catch (Exception e) {
        	ELog.E(e.getMessage());
        }

        return strDate;
    }


    /**
     * 根据指定日期判断星期几
     * 根据指定日期判断星期几
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    public static String GetWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
        	ELog.E(e.getMessage());
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 比较两个日期是否相等
     *
     * @param strDate1 日期1
     * @param strDate2 日期2
     * @return
     */
    public static boolean CompareToDate(String strDate1, String strDate2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar c1 = Calendar.getInstance();

        Calendar c2 = Calendar.getInstance();
        try

        {
            c1.setTime(df.parse(strDate1));
            c2.setTime(df.parse(strDate2));
        } catch (ParseException e) {
        	ELog.E(e.getMessage());
        }

        int result = c1.compareTo(c2);

        if (result == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 得到几天前(后)的时间
     *
     * @param d      以该日期为基准
     * @param day    天数，正数表示后几天，负数表示前几天
     * @param format 日期格式
     * @return
     */
    public static String GetDateBeforeOrAfter(Date d, int day, String format) {
        Calendar now = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format);
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        Date time = now.getTime();
        return df.format(time);
    }

    /**
     * 得到几年前(后)的时间
     *
     * @param d      以该年为基准
     * @param year    天数，正数表示后几年，负数表示前几年
     * @param format 日期格式
     * @return
     */
    public static String GetYearBeforeOrAfter(Date d, int year, String format) {
        Calendar now = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format);
        now.setTime(d);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) - year);
        Date time = now.getTime();
        return df.format(time);
    }

    /**
     * 得到几月前(后)的时间
     *
     * @param d      以该月为基准
     * @param month    月数，正数表示后几月，负数表示前几月
     * @param format 日期格式
     * @return
     */
    public static String GetMonthBeforeOrAfter(Date d, int month, String format) {
        Calendar now = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format);
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) - month);
        Date time = now.getTime();
        return df.format(time);
    }
}
