package com.pushnotification.firebase;

public enum NotificationParameter {

	SOUND("default"),
	COLOR("#FFFF00");
	
	private String value;

	private NotificationParameter(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
		
	
}
