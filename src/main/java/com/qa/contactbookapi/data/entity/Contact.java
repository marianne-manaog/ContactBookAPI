package com.qa.contactbookapi.data.entity;

import java.time.LocalDate;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

// This 'Contact' class includes relevant details of the contact to be added.

@Entity
@Table(name = "Contacts")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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

	public Contact() {
		super();	
	}

	public Contact(Long id, String firstName, String lastName, String mobileNumber, String emailAddress, LocalDate dateOfBirth) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.dateOfBirth = dateOfBirth;
	
	}

	public Contact(String firstName, String lastName, String mobileNumber, String emailAddress, LocalDate dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
		this.dateOfBirth = dateOfBirth;
 
	}
  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	public String toString() {
		return "Contact [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNumber="
				+ mobileNumber + ", emailAddress=" + emailAddress + ", dateOfBirth=" + dateOfBirth + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, emailAddress, firstName, id, lastName, mobileNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;

		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(emailAddress, other.emailAddress)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(mobileNumber, other.mobileNumber);
	}
	
}
