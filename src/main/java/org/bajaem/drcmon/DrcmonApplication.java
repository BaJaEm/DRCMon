
package org.bajaem.drcmon;

import org.apache.activemq.broker.BrokerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.configuration.DRCMonConfiguration;
import org.bajaem.drcmon.engine.MonitorEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableJms
public class DrcmonApplication
{

    private static final Logger LOG = LogManager.getLogger();

    public static void main(final String[] args)
    {
        SpringApplication.run(DrcmonApplication.class, args);
    }

    @Component
    public class MyBean implements CommandLineRunner
    {

        @Autowired
        private BrokerService broker;

        @Autowired
        private MonitorEngine eng;

        @Autowired
        private DRCMonConfiguration config;

        @Override
        public void run(final String... args) throws Exception
        {

            if ( config.autostartEngine())
            {
                eng.start();
            }
            if (config.isTCPBrokerEnabled() && broker.isStopped())
            {
                LOG.info("Initialize broker");
                broker.start();
                LOG.info("started");
            }
        }

    }
}