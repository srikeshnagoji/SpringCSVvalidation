package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Validation_Master {

	@Id
	private int Id;
	@OneToOne
	@JoinColumn(name="cFField")
	private Standard_Object_Attributes cFField;
	@ManyToOne
	@JoinColumn(name="workRequestTypeId")
    private Work_Request_Type_Master workRequestTypeId;
	private boolean isRequired;
	
	
	
	
	
	
	public Validation_Master()
	{
		
	}






	public int getId() {
		return Id;
	}






	public void setId(int id) {
		Id = id;
	}






	public Standard_Object_Attributes getcFField() {
		return cFField;
	}






	public void setcFField(Standard_Object_Attributes cFField) {
		this.cFField = cFField;
	}






	public Work_Request_Type_Master getWorkRequestTypeId() {
		return workRequestTypeId;
	}






	public void setWorkRequestTypeId(Work_Request_Type_Master workRequestTypeId) {
		this.workRequestTypeId = workRequestTypeId;
	}






	public boolean isRequired() {
		return isRequired;
	}






	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}






	public Validation_Master(int id, Standard_Object_Attributes cFField, Work_Request_Type_Master workRequestTypeId,
			boolean isRequired) {
		super();
		Id = id;
		this.cFField = cFField;
		this.workRequestTypeId = workRequestTypeId;
		this.isRequired = isRequired;
	}

	
	
}
