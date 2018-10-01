
package org.bajaem.drcmon.controller;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DRCMonitorController
{

    private static final Logger LOG = LogManager.getLogger();

    @RequestMapping("/monitor")
    public String monitor()
    {
        LOG.debug("Monitor");
        return "<h1>good</h1>";
    }

    @RequestMapping("/user")
    public Principal user(final Principal user)
    {
        return user;
    }
}
