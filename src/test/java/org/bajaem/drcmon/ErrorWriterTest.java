
package org.bajaem.drcmon;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ErrorWriterTest
{

    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void testErrorWriter()
    {
        final Throwable r = new Throwable();
        try (final StringWriter sw = new StringWriter())
        {
            final PrintWriter pw = new PrintWriter(sw);
            r.printStackTrace(pw);
            final String msg = sw.toString();
            assertNotNull(msg);
            pw.close();
        }
        catch (final IOException e)
        {
            LOG.error(e);
        }
    }
}
