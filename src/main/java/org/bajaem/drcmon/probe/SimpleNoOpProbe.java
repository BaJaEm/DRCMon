
package org.bajaem.drcmon.probe;

import org.bajaem.drcmon.model.WebBotProbeConfig;
import org.openqa.selenium.WebDriver;

public class SimpleNoOpProbe extends WebBotAction
{

    public SimpleNoOpProbe(final WebDriver _driver, final WebBotProbeConfig _config)
    {
        super(_driver, _config);
    }

    @Override
    public boolean action()
    {
        return true;
    }

}
