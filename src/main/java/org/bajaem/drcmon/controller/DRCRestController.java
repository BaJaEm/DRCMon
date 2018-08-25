
package org.bajaem.drcmon.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.engine.MonitorEngine;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.bajaem.drcmon.model.ProbeConfig;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(path = "/repo/probeConfig/{id}", method = RequestMethod.GET)
    public ProbeConfig get(@PathVariable("id") long id)
    {
        final ProbeConfig config = (ProbeConfig) repo.findById(id).get();
        return config;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(path = "/repo/probeConfig/{id}", method = RequestMethod.PUT)
    public ProbeConfig save(final String input, @PathVariable("id") long id)
    {
        LOG.info("Got Here: " + id + " \n\n\n\n\n\n\n\n\n" + input);
        final ProbeConfig config = (ProbeConfig) repo.findById(id).get();
        LOG.info("Config: " + config);
        return config;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(path = "/repo/probeConfigs", method = RequestMethod.GET)
    public Iterable<ProbeConfig> getAll()
    {
        final Iterable<ProbeConfig> config = repo.findAll();
        return config;
    }

}
