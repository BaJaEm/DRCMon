
package org.bajaem.drcmon;

import java.net.UnknownHostException;

import org.bajaem.drcmon.engine.MonitorEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DrcmonApplication
{

    public static void main(final String[] args)
    {
        SpringApplication.run(DrcmonApplication.class, args);
    }

    @Component
    public class MyBean implements CommandLineRunner
    {

        @Autowired
        private MonitorEngine eng;

        @Override
        public void run(final String... args) throws UnknownHostException
        {
            //eng.init();
        }

    }

}
