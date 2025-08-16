/**
 * 
 */
package com.iota.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.iota.model.OrderEvent;
import com.iota.model.PaymentEvent;
import com.iota.model.UserEvent;

/**
 * 
 */
@Service
public class MultiEventConsumer {

	@KafkaListener(topics = "user-topic", groupId = "json-consumer-group", containerFactory = "userEventKafkaListenerContainerFactory")
	public void consumeUser(UserEvent event) {
		System.out.println("Received UserEvent: " + event);
	}

	
//	  
//	  @KafkaListener(topics = "user-topic", groupId = "user-group",
//	  containerFactory = "userEventKafkaListenerContainerFactory") public void
//	  consumeUser(UserEvent event) { System.out.println("Received UserEvent: " +
//	  event); }
//	  
//	  @KafkaListener(topics = "order-topic", groupId = "order-group",
//	  containerFactory = "orderEventKafkaListenerContainerFactory") public void
//	  consumeOrder(OrderEvent event) { System.out.println("Received OrderEvent: " +
//	  event); }
//	  
//	  @KafkaListener(topics = "payment-topic", groupId = "payment-group",
//	  containerFactory = "paymentEventKafkaListenerContainerFactory") public void
//	  consumePayment(PaymentEvent event) {
//	  System.out.println("Received PaymentEvent: " + event); }
	 
}
