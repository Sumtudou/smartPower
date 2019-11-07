package com.linln.admin.system.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    /*
     *@Description: 时间戳转date
     * @param stamp 时间戳
     *@return  date
     * @author Sumtudou
     * @date 2019/11/4
    */
    public static Date StampToDate(String stamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i<stamp.length();i++){
            char a = stamp.charAt(i);
            if(a=='T')
                sb.append(' ');
            else if(a!='Z')
                sb.append(a);
        }
        //因为传入的  TZ时区和北京时间相差8小时，所以要加上8个小时
        Date now = sdf.parse(sb.toString());
        Calendar cal =  Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR,8);
        return cal.getTime();
    }
}
