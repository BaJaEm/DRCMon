
package org.bajaem.drcmon.mq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.ProbeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@EnableJms
@Component
public class MessageSender
{

    private static final Logger LOG = LogManager.getLogger();

    public static final String BASE_TOPIC = "drcmon.topic";

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final ProbeResponse resp, final String topic)
    {
        LOG.trace("Sending Message: " + resp);
        final String channel = topic != null ? BASE_TOPIC + "/" + topic : BASE_TOPIC;
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend(channel, resp);
    }

    public void sendMessage(final ProbeResponse resp)
    {
        sendMessage(resp, null);
    }
}
