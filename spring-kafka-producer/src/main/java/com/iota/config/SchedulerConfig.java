package com.iota.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iota.model.UserEvent;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SchedulerConfig {
	
	
	@Value("${app.kafka.user-topic:user-topic}")
	private String userTopic;
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	
	/**
	 * Scheduled method to send messages to Kafka at a fixed rate.
	 * This method is invoked every 5 seconds.
	 * @param kafkaTemplate
	 */
	@Scheduled(fixedRate = 5000)
	public void sendMessage() {
		// Logic to send messages to Kafka
		// Example: kafkaTemplate.send("topicName", "key", "value");
		
		UserEvent userEvent = new UserEvent();
		
		userEvent.setUserId(System.currentTimeMillis());
		userEvent.setEventType("USER_CREATED");
		userEvent.setUserName("User" + System.currentTimeMillis());
		
		log.info("Sending user event: {}", userEvent);
		
		kafkaTemplate.send(userTopic,""+userEvent.getUserId() , userEvent).whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Failed to send event: " + ex.getMessage());
            } else {
                System.out.println("Event sent successfully: " + result.getRecordMetadata());
            }
        }
		);
		
	}

}
