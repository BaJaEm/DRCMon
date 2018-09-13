package org.bajaem.drcmon.mq;

import javax.jms.ConnectionFactory;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class DRCMQConfig
{

    @Bean
    public JmsListenerContainerFactory<?> myFactory(//
            final ConnectionFactory connectionFactory, //
            final DefaultJmsListenerContainerFactoryConfigurer configurer, //
            final MessageConverter messageConverter)
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setClientId("DRCMQ-Client");
        factory.setConcurrency("3-10");
        factory.setMessageConverter(messageConverter);

        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter()
    {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService broker()
    {
        final BrokerService broker = new BrokerService();

        try
        {
            broker.addConnector("tcp://localhost:61616");
            broker.setUseJmx(true);
            final PersistenceAdapter pa = new MemoryPersistenceAdapter();
            broker.setPersistenceAdapter(pa);
            broker.setPersistent(false);

        }
        catch (final Exception e)
        {
            throw new DRCStartupException(e);
        }
        return broker;
    }

}
