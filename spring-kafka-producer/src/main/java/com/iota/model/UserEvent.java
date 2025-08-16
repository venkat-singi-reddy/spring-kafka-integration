/**
 * 
 */
package com.iota.model;

import lombok.Data;

/**
 * @author venkat reddy
 *
 */

@Data
public class UserEvent {
	
	private Long userId;
	
	private String eventType;
	
	private String userName;
	

}
