
package org.bajaem.drcmon.probes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.probe.WebBotAction;
import org.bajaem.drcmon.probe.WebBotProbeConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebBotTestAction extends WebBotAction
{

    private static final Logger LOG = LogManager.getLogger();

    public WebBotTestAction(final WebDriver _driver, final WebBotProbeConfig _config)
    {
        super(_driver, _config);
    }

    @Override
    public boolean action()
    {
        final WebElement msg = driver.findElement(By.tagName("h1"));
        LOG.info(msg.getText());
        return true;
    }
}
