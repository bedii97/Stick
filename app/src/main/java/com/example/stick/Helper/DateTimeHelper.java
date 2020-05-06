package com.example.stick.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {
    public String getDateTime(long timeInMillis){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String dateFormat = sdf.format(date);
        return dateFormat;
    }
}
