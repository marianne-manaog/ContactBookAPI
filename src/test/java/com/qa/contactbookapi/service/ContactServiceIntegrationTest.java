package com.qa.contactbookapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.data.repository.ContactRepository;
import com.qa.contactbookapi.exceptions.InvalidContactException;


@SpringBootTest
@Transactional
public class ContactServiceIntegrationTest {
	
	@Autowired
	private ContactService contactService;

	@Autowired
	private ContactRepository contactRepository;

	private List<Contact> contactsInDatabase;
	
	private Contact firstSavedContact;
	private Contact newContactModifiedFromFirstContactWithoutId;
	
	private long followingNewElementId;
	
	private long idFirstSavedContact;
	
	@BeforeEach
	public void init() {
		List<Contact> contactsList = List.of(
				new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact(2L, "Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact(3L, "Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))
		);
		
		contactsInDatabase = new ArrayList<>();
		contactsInDatabase.addAll(contactRepository.saveAll(contactsList));
				
		firstSavedContact = contactsInDatabase.get(0);
		idFirstSavedContact = firstSavedContact.getId();

		newContactModifiedFromFirstContactWithoutId = new Contact("Katie", "Brockett", "08777777777", "katie.brockett@mycoolmail.com", LocalDate.of(1983, 2, 1));
		
		int sizeContacts = contactsInDatabase.size();
		followingNewElementId = contactsInDatabase.get(sizeContacts - 1).getId() + 1;
	}
	
	@Test
	public void fetchAllContactsTest() {
		assertThat(contactsInDatabase).isEqualTo(contactService.fetchAll());
	}
	
	@Test
	public void fetchContactByIdTest() {
		assertThat(contactService.fetchById(idFirstSavedContact)).isEqualTo(firstSavedContact);
	}
	
	@Test
	public void fetchContactByInvalidIdTest() {
		int invalidId = 55;
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.fetchById(invalidId);
		});
		
		String expected = "Cannot find contact with ID " + invalidId;
		assertThat(e.getMessage()).isEqualTo(expected);
	}

	@Test
	public void fetchContactByLastNameAndFirstNameTest() {
		assertThat(contactService.fetchByLastNameAndFirstName(firstSavedContact.getLastName(), firstSavedContact.getFirstName())).isEqualTo(firstSavedContact);
	}
	
	@Test
	public void generateContactTest() {
		Contact expectedContactPersisted = new Contact(
				followingNewElementId,
				newContactModifiedFromFirstContactWithoutId.getFirstName(),
				newContactModifiedFromFirstContactWithoutId.getLastName(),
				newContactModifiedFromFirstContactWithoutId.getMobileNumber(),
				newContactModifiedFromFirstContactWithoutId.getEmailAddress(),
				newContactModifiedFromFirstContactWithoutId.getDateOfBirth()
				);
		
		assertThat(expectedContactPersisted).isEqualTo(contactService.generate(newContactModifiedFromFirstContactWithoutId));
	}
	
	@Test
	public void editContactByIdTest() {

		Contact contactToEdit = new Contact(
				idFirstSavedContact, 
				newContactModifiedFromFirstContactWithoutId.getFirstName(),
				newContactModifiedFromFirstContactWithoutId.getLastName(),
				newContactModifiedFromFirstContactWithoutId.getMobileNumber(),
				newContactModifiedFromFirstContactWithoutId.getEmailAddress(),
				newContactModifiedFromFirstContactWithoutId.getDateOfBirth()
				);
		
		Contact actual = contactService.editById(idFirstSavedContact, contactToEdit);
		assertThat(actual).isEqualTo(contactToEdit);
	}

	@Test
	public void editContactByLastNameAndFirstNameTest() {
		
		String lastNameFirstSavedContact = firstSavedContact.getLastName();
		String firstNameFirstSavedContact = firstSavedContact.getFirstName();

		Contact contactToEdit = new Contact(
				idFirstSavedContact, 
				firstNameFirstSavedContact,
				lastNameFirstSavedContact,
				newContactModifiedFromFirstContactWithoutId.getMobileNumber(),
				newContactModifiedFromFirstContactWithoutId.getEmailAddress(),
				newContactModifiedFromFirstContactWithoutId.getDateOfBirth()
				);

		Contact actual = contactService.editByLastNameAndFirstName(lastNameFirstSavedContact, firstNameFirstSavedContact, contactToEdit);
		
		assertThat(actual).isEqualTo(contactToEdit);
	}
	
	@Test
	public void removeContactTest() {
		
		contactService.remove(idFirstSavedContact);
		
		assertThat(contactRepository.findById(idFirstSavedContact)).isEqualTo(Optional.empty());
	}
	
	@Test
	public void removeAllContactTest() {
		List<Contact> emptyList = new ArrayList<Contact>();
		
		contactService.removeAll();
		
		assertThat(contactRepository.findAll()).isEqualTo(emptyList);
	}
	
}