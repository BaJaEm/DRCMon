
package org.bajaem.drcmon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.engine.MonitorEngine;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured("ROLE_ADMIN")
public class DRCRestController
{

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    ProbeConfigRepository repo;

    @Autowired
    private MonitorEngine engine;

    @RequestMapping("/start")
    public void start()
    {
        LOG.info("Starting");
        engine.start();
    }

    @RequestMapping("/stop")
    public void stop()
    {
        LOG.info("Stopping...");
        engine.stop();
    }

    @Secured("ROLE_USER")
    @RequestMapping("/status")
    public boolean status()
    {
        return engine.isRunning();
    }
}
