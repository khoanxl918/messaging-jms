package com.example.messagingjms;

import com.example.messagingjms.pojo.Email;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class MessagingJmsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
			SpringApplication.run(MessagingJmsApplication.class, args);

		JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
		System.out.println("Sending email message...");
		jmsTemplate.convertAndSend("mailbox", new Email("abc@def.com", "Hello there!"));
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);

		return factory;
	}

	@Bean
	public MessageConverter jacksonJmsMsgConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}
