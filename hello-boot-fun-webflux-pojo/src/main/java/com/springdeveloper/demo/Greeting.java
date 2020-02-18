package com.springdeveloper.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Greeting {

	private String type;

	private String message;

	public Greeting() {
	}

	public Greeting(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Greeting{" +
				"type='" + type + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
