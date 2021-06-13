package it.isw2.entity;

public class Ticket {
	
	private String key;
	private String date;

	public Ticket(String key, String date) {
		this.key = key;
		this.date = date;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getDate() {
		return this.date;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
