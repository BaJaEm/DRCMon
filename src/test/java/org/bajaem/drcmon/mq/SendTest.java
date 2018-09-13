package org.bajaem.drcmon.mq;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.BrokerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.model.ProbeResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" })

public class SendTest
{
    private static final Logger LOG = LogManager.getLogger();
    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    MessageSender sender;

    @Autowired
    BrokerService broker;

    @Autowired
    ConnectionFactory connectionFactory;

    @Qualifier(value = "myFactory")
    @Autowired
    JmsListenerContainerFactory<?> fact;

    @Autowired
    JmsListenerEndpointRegistry jmsListenerEndpointRegistry;

    @Test
    public void sendMessage() throws InterruptedException, JMSException
    {
        LOG.info("Start...");
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.convertAndSend("drcmon.topic", "Glen");

    }

    @Test
    public void getSender() throws Exception
    {
        assertNotNull(sender);
        final ProbeResponse resp = new ProbeResponse();
        resp.setError("Foo");
        sender.sendMessage(resp);

    }

    @Test
    public void getUnsub() throws Exception
    {
        try
        {
            final ThreadLocal<Session> session = new ThreadLocal<>();
            jmsTemplate.setPubSubDomain(true);

            jmsTemplate.send("drcmon.topic", s ->
            {
                session.set(s);
                return (s.createTextMessage("hello queue world"));
            });

            LOG.info(session.get());

            final ActiveMQSession s = (ActiveMQSession) session.get();
            LOG.info(s.isClosed());

            Collection<MessageListenerContainer> col = jmsListenerEndpointRegistry.getListenerContainers();
            for (MessageListenerContainer cont : col)
            {
                final DefaultMessageListenerContainer dmlc = (DefaultMessageListenerContainer) cont;
                dmlc.destroy();
            }

            jmsTemplate.send("drcmon.topic", p ->
            {

                return (p.createTextMessage("hello queue world"));
            });

            final Connection con = connectionFactory.createConnection();
            assertNotNull(con);
            con.setClientID("DRCClient");
            final Session s1 = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            assertNotNull(s1);
            String topicName = "drcmon.topic";
            String subscriberName = "drcmon.sub";
            final Topic topic = s1.createTopic(topicName);
            assertNotNull(topic);
            final TopicSubscriber subscriber = s1.createDurableSubscriber(topic, subscriberName);
            assertNotNull(subscriber);
            subscriber.close();
            s1.unsubscribe(subscriberName);
            s1.close();
            LOG.info("Unsub");

        }
        catch (Throwable t)
        {
            LOG.fatal(t);
            fail("Execption");
        }
    }
}
