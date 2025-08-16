/**
 * 
 */
package com.iota.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.extern.log4j.Log4j2;

/**
 * @author venkatreddy
 */
@Configuration
@Log4j2
public class ProducerConfig {
	
	
	
	
	
	/**
	 * Creates a KafkaTemplate bean for producing messages to Kafka.
	 *
	 * @param producerFactory the ProducerFactory used to create the KafkaTemplate
	 * @return a KafkaTemplate instance
	 */
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
	

}
