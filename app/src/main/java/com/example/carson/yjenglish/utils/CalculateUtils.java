package com.example.carson.yjenglish.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 84594 on 2018/8/17.
 */

public class CalculateUtils {
    /**
     * 计算几天后的日期
     * @param date 需要计算的该日期
     * @param day 该日期后的天数
     * @return date + day 后的日期
     */
    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

}
