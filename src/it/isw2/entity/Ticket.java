package it.isw2.entity;

/**
 * Entity that models a JIRA ticket
 *
 */
public class Ticket {
	
	private String id;		// Ticket ID
	private String date;	// Ticket creation date

	public Ticket(String key, String date) {
		this.id = key;
		this.date = date;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDate() {
		return this.date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
