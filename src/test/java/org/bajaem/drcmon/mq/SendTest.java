package org.bajaem.drcmon.mq;

import javax.jms.DeliveryMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" })
public class SendTest
{
    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void sendMessage() throws InterruptedException
    {
         jmsTemplate.setPubSubDomain(true);
         jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
         jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.convertAndSend("drcmon.topic", "Glen");
        Thread.sleep(3000);

    }



}
