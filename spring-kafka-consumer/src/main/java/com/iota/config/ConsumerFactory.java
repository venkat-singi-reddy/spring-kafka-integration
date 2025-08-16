/**
 * 
 */
package com.iota.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.iota.model.OrderEvent;
import com.iota.model.PaymentEvent;
import com.iota.model.UserEvent;

/**
 * @author venkatreddy
 *
 */
@Configuration
public class ConsumerFactory {
	

	    @Bean
	    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userEventKafkaListenerContainerFactory() {
	        Map<String, Object> props = new HashMap<>();
	        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093");
	        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json-consumer-group");
	        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
	        
	        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); 
	        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000); 
	        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); 
	        
	        
	        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

	        JsonDeserializer<UserEvent> deserializer = new JsonDeserializer<>(UserEvent.class);
	        //deserializer.addTrustedPackages("*");

	        DefaultKafkaConsumerFactory<String, UserEvent> consumerFactory =
	                new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);

	        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
	        factory.setConsumerFactory(consumerFactory);
	        return factory;
	    }
	    
	    
	  

	        private Map<String, Object> baseProps() {
	            Map<String, Object> props = new HashMap<>();
	            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093");
	            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
	            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
	            return props;
	        }

	        @Bean
	        public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userEventKafkaListenerContainerFactory1() {
	            JsonDeserializer<UserEvent> deserializer = new JsonDeserializer<>(UserEvent.class);
	            //deserializer.addTrustedPackages("*");
	            DefaultKafkaConsumerFactory<String, UserEvent> cf =
	                    new DefaultKafkaConsumerFactory<>(baseProps(), new StringDeserializer(), deserializer);
	            ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
	            factory.setConsumerFactory(cf);
	            return factory;
	        }

	        @Bean
	        public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> orderEventKafkaListenerContainerFactory() {
	            JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<>(OrderEvent.class);
	            //deserializer.addTrustedPackages("*");
	            DefaultKafkaConsumerFactory<String, OrderEvent> cf =
	                    new DefaultKafkaConsumerFactory<>(baseProps(), new StringDeserializer(), deserializer);
	            ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
	            factory.setConsumerFactory(cf);
	            return factory;
	        }

	        @Bean
	        public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentEventKafkaListenerContainerFactory() {
	            JsonDeserializer<PaymentEvent> deserializer = new JsonDeserializer<>(PaymentEvent.class);
	           // deserializer.addTrustedPackages("*");
	            DefaultKafkaConsumerFactory<String, PaymentEvent> cf =
	                    new DefaultKafkaConsumerFactory<>(baseProps(), new StringDeserializer(), deserializer);
	            ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
	            factory.setConsumerFactory(cf);
	            return factory;
	        }
	    

	


}
