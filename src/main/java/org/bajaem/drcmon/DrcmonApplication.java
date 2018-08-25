
package org.bajaem.drcmon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DrcmonApplication
{

    private static final Logger LOG = LogManager.getLogger();

    public static void main(final String[] args)
    {
        SpringApplication.run(DrcmonApplication.class, args);
    }

    // @Component
    // public class MyBean implements CommandLineRunner
    // {
    //
    // @Autowired
    // private MonitorEngine eng;
    //
    // @Override
    // public void run(final String... args) throws UnknownHostException
    // {
    // LOG.info("Initialize engine");
    // eng.start();
    // }
    //
    // }

}
