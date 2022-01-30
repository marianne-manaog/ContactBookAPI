package com.qa.contactbookapi.data.entity;

import java.time.LocalDate;
import java.time.Period;
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
import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Contacts")
public class Contacts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Length(min = 2, message = "First name must be provided.")
	private String firstName;
	
	@NotNull
	@Length(min = 2, message = "Last name must be provided.") 
	private String lastName;
	
	@Size(min = 11, max = 11)
	@NotNull
	private Long mobileNumber;
	
	@Pattern(regexp=".+@.+\\.[a-z]+", message="Email address is invalid.")
	private String emailAddress;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Transient
	private LocalDate dateOfBirth;
	
	@Transient
	private Integer ageFromDateOfBirth;

	public Contacts() {
		super();	
	}

	public Contacts(Long id, @NotNull @Length(min = 2, message = "First name must be provided.") String firstName,
			@NotNull @Length(min = 2, message = "Last name must be provided.") String lastName,
			@Size(min = 11, max = 11) @NotNull Long mobileNumber,
			@Pattern(regexp = ".+@.+\\.[a-z]+", message = "Email address is invalid.") String emailAddress) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
	}

	public Contacts(@NotNull @Length(min = 2, message = "First name must be provided.") String firstName,
			@NotNull @Length(min = 2, message = "Last name must be provided.") String lastName,
			@Size(min = 11, max = 11) @NotNull Long mobileNumber,
			@Pattern(regexp = ".+@.+\\.[a-z]+", message = "Email address is invalid.") String emailAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailAddress = emailAddress;
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

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
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

	public Integer getAgeFromDateOfBirth() {
	      return Period.between(this.dateOfBirth, LocalDate.now()).getYears();

	}
	
	public void setAge(Integer ageFromDateOfBirth) {
		this.ageFromDateOfBirth = ageFromDateOfBirth; 
	}

	@Override
	public String toString() {
		return "Contacts [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNumber="
				+ mobileNumber + ", emailAddress=" + emailAddress + ", dateOfBirth=" + dateOfBirth
				+ ", ageFromDateOfBirth=" + ageFromDateOfBirth + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(ageFromDateOfBirth, dateOfBirth, emailAddress, firstName, id, lastName, mobileNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contacts other = (Contacts) obj;
		return Objects.equals(ageFromDateOfBirth, other.ageFromDateOfBirth)
				&& Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(emailAddress, other.emailAddress)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(mobileNumber, other.mobileNumber);
	}
	
}
