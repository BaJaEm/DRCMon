package org.bajaem.drcmon.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class MyHandler implements SessionAwareMessageListener<Message>
{
    private static final Logger LOG = LogManager.getLogger();

    @JmsListener(destination = "drcmon.topic", subscription = "drcmon.sub", containerFactory = "myFactory")
    @Override
    public void onMessage(final Message _message, final Session session) throws JMSException
    {
        LOG.info(_message);

    }

}
