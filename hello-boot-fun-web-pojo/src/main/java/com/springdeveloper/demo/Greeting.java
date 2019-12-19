package com.springdeveloper.demo;

public class Greeting {

	private String type;

	private String name;

	public Greeting(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Greeting{" +
				"type='" + type + '\'' +
				", message='" + name + '\'' +
				'}';
	}
}
