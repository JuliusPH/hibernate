package com.exist.model;

import com.exist.model.enums.ContactType;

public class Contact extends BaseEntity{
	private ContactType contactType;
	private String value;

	public Contact(){}

	public Contact(ContactType contactType, String value){
		this.contactType = contactType;
		this.value = value;
	}

	public ContactType getContactType(){
		return contactType;
	}

	public void setContactType(ContactType contactType){
		this.contactType = contactType;
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}
}
