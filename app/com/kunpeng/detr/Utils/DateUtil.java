package com.kunpeng.detr.Utils;

import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/3.
 */
public class DateUtil {

    /// <summary>
    /// 将18SEP转换成DateTime
    /// </summary>
    /// <param name="date"></param>
    /// <returns></returns>
    public static HashMap<String, String> getMonthMap(){
        HashMap<String, String> MonthMap = Maps.newHashMap();
        MonthMap.put("JAN", "01");
        MonthMap.put("FEB", "02");
        MonthMap.put("MAR", "03");
        MonthMap.put("APR", "04");
        MonthMap.put("MAY", "05");
        MonthMap.put("JUN", "06");
        MonthMap.put("JUL", "07");
        MonthMap.put("AUG", "08");
        MonthMap.put("SEP", "09");
        MonthMap.put("OCT", "10");
        MonthMap.put("NOV", "11");
        MonthMap.put("DEC", "12");
        return MonthMap;

    }

    //o8JAN, 是否在今日之前，
    public static Date DateCovCh(String date, Boolean isBeforeNow)
    {
        // 08JAN 转成 2017-01-08 00:00:00.00
        //Date CandDate = DateTime.now().toDate();
        //如果月比今年的月还大，就加1
        HashMap<String, String> MonthMap = getMonthMap();
        String day = date.substring(0, 2);
        String month = date.substring(2, 5);
        String monthNum = MonthMap.get(month);
        DateTime now = DateTime.now();
        int year = now.getYear();

        DateTime departureDate = DateTime.parse(year + "-" + monthNum + "-" + day);
        if(isBeforeNow && departureDate.isAfterNow()){
            departureDate = departureDate.minusYears(1);
        }else if(!isBeforeNow && departureDate.isBeforeNow()){
            departureDate = departureDate.plusYears(1);
        }
        return departureDate.toDate();
    }
}
