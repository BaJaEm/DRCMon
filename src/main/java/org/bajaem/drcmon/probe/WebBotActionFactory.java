
package org.bajaem.drcmon.probe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.model.WebBotProbeConfig;
import org.openqa.selenium.WebDriver;

public class WebBotActionFactory
{

    private static final Logger LOG = LogManager.getLogger();

    public static WebBotAction createWebBotAction(final WebDriver driver, final WebBotProbeConfig config)
    {
        try
        {
            final Class<? extends WebBotAction> _clazz = config.getActionScript();
            final Constructor<? extends WebBotAction> constructor = _clazz.getConstructor(WebDriver.class,
                    WebBotProbeConfig.class);
            return constructor.newInstance(driver, config);
        }
        catch (final NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e)
        {
            LOG.fatal(e);
            throw new DRCStartupException(e);
        }
    }
}
