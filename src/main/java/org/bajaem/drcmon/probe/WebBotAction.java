
package org.bajaem.drcmon.probe;

import org.bajaem.drcmon.model.WebBotProbeConfig;
import org.openqa.selenium.WebDriver;

public abstract class WebBotAction
{

    protected final WebDriver driver;

    protected final WebBotProbeConfig config;

    public WebBotAction(final WebDriver _driver, final WebBotProbeConfig _config)
    {
        driver = _driver;
        config = _config;
    }

    public abstract boolean action();
}
