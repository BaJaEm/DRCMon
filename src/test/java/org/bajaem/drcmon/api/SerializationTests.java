
package org.bajaem.drcmon.api;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.PingProbeConfig;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationTests
{

    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void testProbeConfig()
    {
        final ObjectMapper m = new ObjectMapper();
        final PingProbeConfig conf = new PingProbeConfig();
        conf.setId(9999);
        conf.setArtifactId("NONE");
        conf.setPollingInterval(30);
        conf.setDelayTime(0);
        conf.setCreatedOn(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        conf.setCreatedBy("DUDE");
        conf.setLastModifiedOn(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        conf.setLastModifiedBy("DUDE");
        conf.setCustomConfiguration(new HashMap());
        final JsonNode node = m.convertValue(conf, JsonNode.class);
        LOG.info(node);

    }
}
