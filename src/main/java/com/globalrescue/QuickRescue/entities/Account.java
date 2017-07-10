package com.globalrescue.QuickRescue.entities;

import java.util.Set;

public class Account {
	
	private int accountId;
	private String name;
	private String emailDomain;
	private String timeZoneCity;
	
	private Set<Contact> contacts;
	
	public Account() {
		
	}
	
	public Account(String name, String emailDomain, String timeZoneCity) {
		this.name = name;
		this.emailDomain = emailDomain;
		this.timeZoneCity = timeZoneCity;
	}
	
	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public String getTimeZoneCity() {
		return timeZoneCity;
	}

	public void setTimeZoneCity(String timeZoneCity) {
		this.timeZoneCity = timeZoneCity;
	}
	
	

}
