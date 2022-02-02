package com.qa.contactbookapi.controller.request_object;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

public class ContactRequest {

	@NotNull
	@Length(min = 2, message = "First name must be provided.")
	private String firstName;
	
	@NotNull
	@Length(min = 2, message = "Last name must be provided.") 
	private String lastName;
	
	@Size(min = 11, max = 11, message = "Mobile number must be 11-digit long (when dialling within the UK).")
	@NotNull
	private String mobileNumber;
	
	@Pattern(regexp=".+@.+\\.[a-z]+", message="Email address is invalid.")
	private String emailAddress;
	
	private LocalDate dateOfBirth;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, emailAddress, firstName, lastName, mobileNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactRequest other = (ContactRequest) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(emailAddress, other.emailAddress)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(mobileNumber, other.mobileNumber);
	}
	
}
