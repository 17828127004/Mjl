package util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/12.
 */
public class TimeUtil {
        // 一天的毫秒数
        public static final long MILLI_OF_DAY = 1000 * 60 * 60 * 24;

        /**
         *
         * @param d1
         * @param d2
         * @return是否同一天
         */
        public static boolean isSameDay(Date d1, Date d2) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            return sd.format(d1).equals(sd.format(d2));
        }

        // 是否同一月
        public static boolean isSameMonth(Date d1, Date d2) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
            return sd.format(d1).equals(sd.format(d2));
        }

        /**
         * 时间格式转换函数
         */
        public static String toTimeStr(long time) {
            if (time < 0) {
                return "-:-";
            }

            if (time == 0) {
                return "00:00";
            }

            time /= 1000;
            long minute = time / 60;
            long hour = minute / 60;
            long second = time % 60;
            minute %= 60;

            if (hour <= 0) {
                return String.format("%02d:%02d", minute, second);
            } else {
                return String.format("%02d:%02d:%02d", hour, minute, second);
            }
        }

        /**
         *
         * @param date
         * @return 获取某时间到现在的时间状态
         */
        public static String getDaysBeforeNow(Date date) {
            if (date == null) {
                return "--";
            }

            long sysTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date()));
            long ymdhms = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(date));
            String strYear = "年前";
            String strMonth = "月前";
            String strDay = "天前";
            String strHour = "小时前";
            String strMinute = "分钟前";
            try {
                if (ymdhms == 0) {
                    return "--";
                }
                long between = (sysTime / 10000000000L) - (ymdhms / 10000000000L);
                if (between > 0) {
                    return between + strYear;
                }
                between = (sysTime / 100000000L) - (ymdhms / 100000000L);
                if (between > 0) {
                    return between + strMonth;
                }
                between = (sysTime / 1000000L) - (ymdhms / 1000000L);
                if (between > 0) {
                    return between + strDay;
                }
                between = (sysTime / 10000) - (ymdhms / 10000);
                if (between > 0) {
                    return between + strHour;
                }
                between = (sysTime / 100) - (ymdhms / 100);
                if (between > 0) {
                    return between + strMinute;
                }
                return "1" + strMinute;
            } catch (Exception e) {
                return "";
            }
        }

        /**
         *
         * @param
         * @return 获取某时间到现在的时间状态
         */
        public static String getDaysBeforeNow(String sdate) {
            Date date = strToDate("yyyy-MM-dd HH:mm", sdate);
            if (date == null) {
                return "--";
            }

            long sysTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date()));
            long ymdhms = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(date));
            String strYear = "年";
            String strMonth = "月";
            String strDay = "天";
            try {
                if (ymdhms == 0) {
                    return "--";
                }
                long between = (sysTime / 10000000000L) - (ymdhms / 10000000000L);
                if (between > 0) {
                    return between + strYear;
                }
                between = (sysTime / 100000000L) - (ymdhms / 100000000L);
                if (between > 0) {
                    return between + strMonth;
                }
                between = (sysTime / 1000000L) - (ymdhms / 1000000L);
                if (between > 0) {
                    return between + strDay;
                }
                return "0" + strDay;
            } catch (Exception e) {
                return "";
            }
        }


        /***************** 时间相关获取 ************/
        /**
         * @return获取当前时间String年-月-日 时：分：秒
         */
        public static String getCurTime() {
            Date now = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(now);
            return dateNowStr;
        }
        /**
         * 从服务器获取的时间，与当前时间相差
         * @param currentTime
         * @return
         */

        @SuppressLint("SimpleDateFormat")
        public  static long differ(String currentTime){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long a=0;
            try {
                Date date = sdf.parse(currentTime);
                long dd= date.getTime();
                long temp=24*60*60*1000;
                Date date2=sdf.parse(getCurTime());
                long cc=date2.getTime();
                a= (cc-dd)/temp;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return a;
        }


        /**
         *
         * @return获取当前号数
         */
        public static int getDay() {
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_MONTH);
            return day;
        }

        /**
         * @return获取当前月份
         */
        public static int getMonth() {
            Calendar now = Calendar.getInstance();
            int month = now.get(Calendar.MONTH);
            return month;
        }

        /**
         * @return获取当前年
         */
        public static int getYear() {
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            return year;
        }

        /**
         * @return获取当月天数
         */
        public static int getMonthOfDay() {
            Calendar now = Calendar.getInstance();
            int dateOfMonth = now.getActualMaximum(Calendar.DATE);
            return dateOfMonth;
        }

        /**
         * @return获取指定月份天数
         */
        public static int getAMonthOfDay(int year, int month) {
            Calendar now = Calendar.getInstance();
            now.set(Calendar.YEAR, year);
            now.set(Calendar.MONTH, month - 1);// Java月份才0开始算
            int dateOfMonth = now.getActualMaximum(Calendar.DATE);
            return dateOfMonth;
        }

        /**
         * @return字符串类型日期转化成date类型
         */
        public static Date strToDate(String style, String date) {
            SimpleDateFormat formatter = new SimpleDateFormat(style);
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }

        public static String dateToStr(String style, Date date) {
            SimpleDateFormat formatter = new SimpleDateFormat(style);
            return formatter.format(date);
        }

        /**
         * 根据时间类型比较时间大小
         *
         * @param source
         * @param traget
         * @param type
         *            "YYYY-MM-DD" "yyyyMMdd HH:mm:ss" 类型可自定义
         *
         * @return 0 ：source和traget时间相同 ,1 ：source比traget时间大, -1：source比traget时间小
         * @throws Exception
         */
        public static int DateCompare(String source, String traget, String type)
                throws Exception {
            int ret = 2;
            SimpleDateFormat format = new SimpleDateFormat(type);
            Date sourcedate = format.parse(source);
            Date tragetdate = format.parse(traget);
            ret = sourcedate.compareTo(tragetdate);
            return ret;
        }

        /**
         * 获取当前日期附近(后)的某天
         *
         * @param i
         * @return
         */
        public static String nearDate(int i) {
            Calendar cal1 = Calendar.getInstance();// 获取当前日期
            cal1.add(Calendar.DATE, i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nearDay = sdf.format(cal1.getTime());
            return nearDay;
        }
        /**
         * 获取当前日期附近（前）的某天
         *
         * @param i
         * @return
         */
        public static String reduceDate(int i) {
            Calendar cal1 = Calendar.getInstance();// 获取当前日期
            cal1.add(Calendar.DATE, -i);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nearDay = sdf.format(cal1.getTime());
            return nearDay;
        }

        public String makeTime(int year, int mon, int day) {
            StringBuffer time = new StringBuffer();
            time.append(year);
            if (mon <= 9) {
                time.append("-" + "0" + mon);
            } else {
                time.append("-" + mon);
            }
            if (day <= 9) {
                time.append("-" + "0" + day);
            } else {
                time.append("-" + day);
            }

            return time.toString();
        }

        public String makeTime(int year, int mon) {
            StringBuffer time = new StringBuffer();
            time.append(year);
            if (mon <= 9) {
                time.append("-" + "0" + mon);
            } else {
                time.append("-" + mon);
            }
            return time.toString();
        }

        public int getWeekOfDate(Date dt) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            return w;
        }

        public Calendar addDayOfDate(int addDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, addDay);
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
            return calendar;
        }

        public Calendar reduceDayOfDate(int reduceDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

            calendar.add(Calendar.DAY_OF_MONTH, -reduceDay);
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
            return calendar;
        }

        public Calendar addDayOfDate(int year, int month, int day, int addDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, addDay);
            System.out.println(df.format(calendar.getTime()));
            return calendar;
        }

        public static String addDayOfDate(Date date, int addDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, addDay);
            System.out.println(df.format(calendar.getTime()));
            return df.format(calendar.getTime());
        }

        public Calendar reduceDayOfDate(int year, int month, int day, int reduceDay) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, -reduceDay);
            System.out.println(df.format(calendar.getTime()));
            return calendar;
        }
    //getTime方法返回的就是10位的时间戳

    public static  String getTime(){

        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳

        String  str=String.valueOf(time);

        return str;

    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}
