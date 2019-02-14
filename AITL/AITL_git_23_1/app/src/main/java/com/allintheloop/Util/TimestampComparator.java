package com.allintheloop.Util;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by nteam on 4/8/17.
 */

public class TimestampComparator implements Comparator<Object> {

    @Override
    public int compare(Object o1, Object o2) {
        String time1 = "", time2 = "";
        Date date1, date2;
//        if(o1 instanceof Activity_Internal_Feed)
//        {
//            time1 = ((Activity_Internal_Feed) o1).getTimestamp();
//        }
//        else  if(o1 instanceof Activity_Facebook_Feed)
//        {
//            time1 = ((Activity_Facebook_Feed) o1).getTimestamp();
//        }
//        else  if(o1 instanceof Activity_Twitter_Feed)
//        {
//            time1 = ((Activity_Twitter_Feed) o1).getTimestamp();
//        }
//        else  if(o1 instanceof Activity_Instagram_Feed)
//        {
//            time1 = ((Activity_Instagram_Feed) o1).getTimestamp();
//        }
//
//        if(o2 instanceof Activity_Internal_Feed )
//        {
//            time2 = ((Activity_Internal_Feed) o2).getTimestamp();
//        }
//        else  if(o2 instanceof Activity_Facebook_Feed)
//        {
//            time2 = ((Activity_Facebook_Feed) o2).getTimestamp();
//        }
//        else  if(o2 instanceof Activity_Twitter_Feed)
//        {
//            time2 = ((Activity_Twitter_Feed) o2).getTimestamp();
//        }
//        else  if(o2 instanceof Activity_Instagram_Feed)
//        {
//            time2 = ((Activity_Instagram_Feed) o2).getTimestamp();
//        }


        date1 = new Date(Long.parseLong(time1));
        date2 = new Date(Long.parseLong(time2));

        return date2.compareTo(date1);
    }
}