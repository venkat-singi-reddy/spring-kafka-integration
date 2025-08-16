/**
 * 
 */
package com.iota.model;

import lombok.Data;

/**
 * 
 */
@Data
public class PaymentEvent {
	
	private Integer paymentId;
	
	private String paymentType;
	
	private String paymentStatus;

}
