package org.bajaem.netmon.drcmon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.netmon.drcmon.probe.PingProbe;

public class Main
{
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static final InetAddress localhost;

    static
    {
        try
        {
            localhost = InetAddress.getLocalHost();
        }
        catch (final UnknownHostException e)
        {
            // This should not happen - we should *ALWAYS* be able to get the
            // localhost assuming we are on the network.
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) throws UnknownHostException
    {
        for (int x = 2; x < 254; x += 1)
        {
            run(1000, x);
        }
    }

    public static void run(final int t, final int second) throws UnknownHostException
    {
        final Calendar start = Calendar.getInstance();
        LOG.trace("Starting...");
        final ScheduledExecutorService ses = Executors.newScheduledThreadPool(t);
        byte[] addr = new byte[4];
        addr[0] = 10;
        int a = 0;
        for (int z = 1; z < second; z++)
        {
            addr[1] = (byte) z;
            ;
            for (int y = 1; y < 255; y++)
            {
                addr[2] = (byte) y;
                for (int x = 1; x < 255; x++)
                {
                    addr[3] = (byte) x;
                    //final PingProbe pLocal = new PingProbe(300, InetAddress.getByAddress(addr));
                    // ses.scheduleAtFixedRate(pLocal, 60,
                    // pLocal.getPollingInterval(), TimeUnit.SECONDS);
                   // ses.execute(pLocal);
                    a++;
                }
            }
        }

        LOG.trace("Waiting...");

        try
        {
            Thread.sleep(100);
            LOG.trace("Shutting down");
            ses.shutdown();
            ses.awaitTermination(30, TimeUnit.MINUTES);
            LOG.trace(start);
            LOG.trace("Complete");
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Calendar end = Calendar.getInstance();
        LOG.info((end.getTimeInMillis() - start.getTimeInMillis()) + " for " + a + " with " + t + " threads");

    }
}
