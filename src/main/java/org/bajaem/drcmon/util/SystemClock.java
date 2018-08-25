package org.bajaem.drcmon.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class SystemClock
{

    public static Timestamp currentTime()
    {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }
}
