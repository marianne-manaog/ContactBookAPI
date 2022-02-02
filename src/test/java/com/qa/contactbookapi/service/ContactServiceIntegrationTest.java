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
	private long followingNewElementId;
	
	@BeforeEach
	public void init() {
		List<Contact> contactsList = List.of(
				new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact(2L, "Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact(3L, "Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))
		);
		contactsInDatabase = new ArrayList<>();
		
		contactsInDatabase.addAll(contactRepository.saveAll(contactsList));
		
		int sizeContacts = contactsInDatabase.size();
		followingNewElementId = contactsInDatabase.get(sizeContacts - 1).getId() + 1;
	}
	
	@Test
	public void fetchAllContactsTest() {
		assertThat(contactsInDatabase).isEqualTo(contactService.fetchAll());
	}
	
	@Test
	public void fetchContactByIdTest() {
		Contact savedContact = contactsInDatabase.get(0);
		assertThat(contactService.fetchById(savedContact.getId())).isEqualTo(savedContact);
	}
	
	@Test
	public void fetchContactByInvalidIdTest() {
		int id = 55;
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.fetchById(id);
		});
		
		String expected = "Cannot find contact with ID " + id;
		assertThat(e.getMessage()).isEqualTo(expected);
	}

	@Test
	public void fetchContactByLastNameAndFirstNameTest() {
		Contact savedContact = contactsInDatabase.get(0);
		assertThat(contactService.fetchByLastNameAndFirstName(savedContact.getLastName(), savedContact.getFirstName())).isEqualTo(savedContact);
	}
	
	@Test
	public void generateContactTest() {
		Contact contactToPersist = new Contact("Katie", "Brockett", "08777777777", "katie.brockett@mycoolmail.com", LocalDate.of(1983, 2, 1));
		Contact expectedContactPersisted = new Contact(
				followingNewElementId,
				contactToPersist.getFirstName(),
				contactToPersist.getLastName(),
				contactToPersist.getMobileNumber(),
				contactToPersist.getEmailAddress(),
				contactToPersist.getDateOfBirth()
				);
		
		assertThat(expectedContactPersisted).isEqualTo(contactService.generate(contactToPersist));
	}
	
	@Test
	public void editContactByIdTest() {
		Contact savedContact = contactsInDatabase.get(0);
		long id = savedContact.getId();
		Contact contactToEdit = new Contact(savedContact.getId(), 
				savedContact.getFirstName(),
				savedContact.getLastName(),
				savedContact.getMobileNumber(),
				savedContact.getEmailAddress(),
				savedContact.getDateOfBirth()
				);
		
		Contact actual = contactService.editById(id, contactToEdit);
		assertThat(actual).isEqualTo(contactToEdit);
	}

	@Test
	public void editContactByLastNameAndFirstNameTest() {
		Contact savedContact = contactsInDatabase.get(0);
		String lastName = savedContact.getLastName();
		String firstName = savedContact.getFirstName();
		Contact contactToEdit = new Contact(savedContact.getId(), 
				firstName,
				lastName,
				savedContact.getMobileNumber(),
				savedContact.getEmailAddress(),
				savedContact.getDateOfBirth()
				);

		Contact actual = contactService.editByLastNameAndFirstName(lastName, firstName, savedContact);
		assertThat(actual).isEqualTo(contactToEdit);
	}
	
	@Test
	public void removeContactTest() {
		Contact savedContact = contactsInDatabase.get(0);
		long id = savedContact.getId();
		
		contactService.remove(id);
		
		assertThat(contactRepository.findById(id)).isEqualTo(Optional.empty());
	}
	
	@Test
	public void removeAllContactTest() {
		List<Contact> emptyList = new ArrayList<Contact>();
		
		contactService.removeAll();
		
		assertThat(contactRepository.findAll()).isEqualTo(emptyList);
	}
	
}