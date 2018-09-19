
package org.bajaem.drcmon.probe;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.bajaem.drcmon.configuration.ProbeMarker;
import org.bajaem.drcmon.model.WebBotProbeConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@ProbeMarker(config = WebBotProbeConfig.class, typeName = "WebBot")

public class WebBotProbe extends Probe
{

    private static final Logger LOG = LogManager.getLogger();

    private final WebBotProbeConfig myConfig;

    protected WebDriver driver;

    public WebBotProbe(final WebBotProbeConfig _probeConfig)
    {
        super(_probeConfig);
        myConfig = _probeConfig;
    }

    @Override
    public Response probe()
    {
        final String uuid = UUID.randomUUID().toString();
        final File tempDir = new File("temp/", uuid);
        tempDir.mkdirs();
        tempDir.deleteOnExit();

        try
        {
            try
            {
                System.setProperty("webdriver.chrome.driver", myConfig.getWebDriverPath());
                final ChromeOptions opts = new ChromeOptions();
                if (myConfig.isHeadless())
                {
                    opts.addArguments("--headless");
                }

                opts.addArguments("user-data-dir=" + tempDir.getAbsolutePath());
                opts.addArguments("incognito");

                driver = new ChromeDriver(opts);
                LOG.debug("Timeout: " + myConfig.getWebDriverTimeout());
                driver.manage().timeouts().implicitlyWait(myConfig.getWebDriverTimeout(), TimeUnit.SECONDS);
                driver.manage().deleteAllCookies();
                driver.get(myConfig.getUrl());
                LOG.debug("End Start");
                LOG.debug("Start action");
                final WebBotAction script = WebBotActionFactory.createWebBotAction(driver, myConfig);
                script.action();
                LOG.debug("End action");
            }
            catch (final Throwable e)
            {
                LOG.error(e);
                if (LOG.isTraceEnabled())
                {
                    LOG.trace(driver.getPageSource());
                }
                return new Response(false, e.getMessage(), e);
            }
            finally
            {

                driver.quit();
                LOG.debug("Shutting down driver");

                boolean failed = false;
                int retries = 3;
                do
                {
                    try
                    {
                        Thread.sleep(1000);
                        FileUtils.deleteDirectory(tempDir);
                        failed = false;
                    }
                    catch (final Exception e)
                    {
                        failed = true;
                        retries--;

                        Thread.sleep(10000);

                        LOG.warn(e.getMessage(), e);
                    }

                }
                while (failed && (retries > 0));
                if (failed)
                {
                    LOG.error("failed to delete: " + tempDir);
                }
                ThreadContext.pop();
            }

            return new Response(true);
        }
        catch (final Exception e)
        {
            return new Response(false, e.getMessage(), e);
        }

    }

    @Override
    public String getUniqueKey()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
