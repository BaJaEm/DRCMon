
package org.bajaem.drcmon.controller;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Principal user(Principal principal)
    {
        return principal;
    }

    @RequestMapping("/auth")
    public Authentication user()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    @RequestMapping("/ctx")
    public SecurityContext ctx()
    {
        return SecurityContextHolder.getContext();
    }
}
