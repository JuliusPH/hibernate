package com.exist.dto;

import com.exist.model.enums.ContactType;

public class ContactDto extends BaseEntityDto{
	private ContactType contactType;
	private String value;

	public ContactDto(){}

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
