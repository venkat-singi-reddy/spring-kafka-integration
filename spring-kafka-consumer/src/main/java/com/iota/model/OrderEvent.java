/**
 * 
 */
package com.iota.model;

import lombok.Data;

/**
 * 
 */
@Data
public class OrderEvent {
	
	private Integer orderId;
	
	private String orderType;
	
	private String orderStatus;

}
