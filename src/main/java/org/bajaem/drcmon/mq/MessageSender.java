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

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final ProbeResponse resp)
    {
        LOG.trace("Sending Message: " + resp);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend("drcmon.topic", resp);
    }
}
