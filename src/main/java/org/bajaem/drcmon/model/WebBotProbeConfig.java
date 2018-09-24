
package org.bajaem.drcmon.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.ProbeMarkerCache;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.mq.MessageSender;
import org.bajaem.drcmon.probe.WebBotAction;

public class WebBotProbeConfig extends URLBasedConfig
{

    private static final Logger LOG = LogManager.getLogger();

    public static final String WEBDRIVER_KEY = "WEBDRIVER";

    public static final String DEFAULT_WEBDRIVER_LOC = "bin/";

    public static final String HEADLESS_KEY = "HEADLESS";

    public static final String TIMEOUT_KEY = "TIMEOUT";

    public static final int DEFAULT_TIMEOUT = 90;

    public static final String ACTIONSCRIPT_KEY = "ACTIONSCRIPT";

    public WebBotProbeConfig(final ProbeConfig _config, final ProbeMarkerCache _cache, final MessageSender _sender)
    {
        super(_config, _cache, _sender);
    }

    public String getWebDriverPath()
    {
        final String loc = getConfig().getCustomConfiguration().get(WEBDRIVER_KEY);
        if (loc == null)
        {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows"))
            {
                return DEFAULT_WEBDRIVER_LOC + "chromedriver.exe";
            }
            else if (os.contains("linux"))
            {
                return DEFAULT_WEBDRIVER_LOC + "chromedriver";

            }
            else
            {
                throw new DRCStartupException(os + " is not a supported operating system");
            }
        }
        return loc;
    }

    public void setWebDriver(final String _webDriverPath)
    {
        getConfig().getCustomConfiguration().put(WEBDRIVER_KEY, _webDriverPath);
    }

    public boolean isHeadless()
    {
        final String headless = getConfig().getCustomConfiguration().get(HEADLESS_KEY);

        return (headless != null && headless.toLowerCase().equals("false")) ? false : true;
    }

    public void setHeadless(final boolean _headless)
    {
        getConfig().getCustomConfiguration().put(HEADLESS_KEY, _headless ? "true" : "false");
    }

    public int getWebDriverTimeout()
    {
        final String to = getConfig().getCustomConfiguration().get(TIMEOUT_KEY);
        return to != null ? Integer.parseInt(to) : DEFAULT_TIMEOUT;
    }

    public void setWebDriverTimeout(final int _timeout)
    {
        getConfig().getCustomConfiguration().put(TIMEOUT_KEY, Integer.toString(_timeout));
    }

    public Class<? extends WebBotAction> getActionScript()
    {
        try
        {
            LOG.debug(getConfig().getCustomConfiguration().get(ACTIONSCRIPT_KEY));
            return (Class<? extends WebBotAction>) Class
                    .forName(getConfig().getCustomConfiguration().get(ACTIONSCRIPT_KEY));
        }
        catch (final ClassNotFoundException e)
        {
            throw new DRCStartupException(e);
        }
    }

    public void setActionScript(final Class<? extends WebBotAction> _actionScript)
    {
        getConfig().getCustomConfiguration().put(ACTIONSCRIPT_KEY, _actionScript.getCanonicalName());
    }

}
