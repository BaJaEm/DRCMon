package org.bajaem.drcmon.util;

import java.sql.Timestamp;
import java.time.Instant;

public class SystemClock
{

    public static Timestamp currentTime()
    {
        return new Timestamp(Instant.now().toEpochMilli());
    }
}