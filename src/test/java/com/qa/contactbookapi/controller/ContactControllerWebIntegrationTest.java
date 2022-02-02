package com.qa.contactbookapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.service.ContactService;


@WebMvcTest(ContactController.class)
public class ContactControllerWebIntegrationTest {

	@Autowired
	private ContactController controller;
	
	@MockBean
	private ContactService contactService;
	
	private List<Contact> contacts;
	private Contact contactToCreate;
	private Contact validContact;
	private Contact updatedContactWithId;
	private Contact updatedContactWithoutId;
	
	@BeforeEach
	public void init() {
		contacts = new ArrayList<>();
		contacts.addAll(List.of(
				new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact(2L, "Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact(3L, "Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))));
		
		contactToCreate = new Contact("Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
		validContact = new Contact(4L, "Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
		
		updatedContactWithId = new Contact(1L, "Katrina", "Becketts", "07777777779", "katrina.becketts@mycoolmail.com", LocalDate.of(1992, 4, 6));
		updatedContactWithoutId = new Contact("Katrina", "Becketts", "07777777779", "katrina.becketts@mycoolmail.com", LocalDate.of(1992, 4, 6));
	}
	
	@Test
	public void fetchAllContactsTest() {
		ResponseEntity<List<Contact>> expected = new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);

		when(contactService.fetchAll()).thenReturn(contacts);
		
		ResponseEntity<List<Contact>> actual = controller.fetchContacts();
		assertThat(expected).isEqualTo(actual);
		
		verify(contactService, times(1)).fetchAll();
	}
	
	@Test
	public void fetchContactByIdTest() {
		ResponseEntity<Contact> expected = ResponseEntity.of(Optional.of(validContact));
		
		when(contactService.fetchById(1)).thenReturn(validContact);
		
		ResponseEntity<Contact> actual = controller.fetchContactById(1);
		
		assertEquals(expected, actual);
		
		verify(contactService, times(1)).fetchById(1);
	}
	
	@Test
	public void fetchContactByLastNameAndFirstNameTest() {
		ResponseEntity<Contact> expected = ResponseEntity.of(Optional.of(validContact));
		
		when(contactService.fetchByLastNameAndFirstName("Beckett", "Kate")).thenReturn(validContact);
		
		ResponseEntity<Contact> result = controller.fetchContactByLastNameAndFirstName("Beckett", "Kate");
		
		assertEquals(expected, result);
		
		verify(contactService, times(1)).fetchByLastNameAndFirstName("Beckett", "Kate");
	}
	
	@Test
	public void generateContactTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + String.valueOf(validContact.getId()));
		ResponseEntity<Contact> expected = new ResponseEntity<Contact>(validContact, headers, HttpStatus.CREATED);
		
		when(contactService.generate(contactToCreate)).thenReturn(validContact);
		
		ResponseEntity<Contact> actual = controller.generateContact(contactToCreate);
		assertEquals(expected, actual);
		
		verify(contactService).generate(contactToCreate);
	}
	
	@Test
	public void editContactByIdTest() {
		
		long contactId = updatedContactWithId.getId();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + String.valueOf(contactId));
		ResponseEntity<Contact> expected = new ResponseEntity<Contact>(updatedContactWithId, headers, HttpStatus.ACCEPTED);
		
		when(contactService.editById(contactId, updatedContactWithoutId)).thenReturn(updatedContactWithId);
		
		ResponseEntity<Contact> result = controller.editContactById(contactId, updatedContactWithoutId);
		
		assertEquals(expected, result);
		verify(contactService).editById(contactId, updatedContactWithoutId);
	}
	
	@Test
	public void editContactByLastNameAndFirstNameTest() {
		
		String lastName = updatedContactWithId.getLastName();
		String firstName = updatedContactWithId.getFirstName();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + String.valueOf(firstName) + '/' + String.valueOf(lastName));
		ResponseEntity<Contact> expected = new ResponseEntity<Contact>(updatedContactWithId, headers, HttpStatus.ACCEPTED);
		
		when(contactService.editByLastNameAndFirstName(lastName, firstName, updatedContactWithoutId)).thenReturn(updatedContactWithId);
		
		ResponseEntity<Contact> result = controller.editContactByLastNameAndFirstName(lastName, firstName, updatedContactWithoutId);
		
		assertEquals(expected, result);
		verify(contactService).editByLastNameAndFirstName(lastName, firstName, updatedContactWithoutId);
	}
	
	@Test
	public void removeContactTest() {
		
		long contactId = 1;
		ResponseEntity<?> expected = ResponseEntity.accepted().build();
		ResponseEntity<?> result = controller.removeContact(contactId);
		
		assertEquals(expected, result);
		verify(contactService).remove(contactId);
	}
	
	@Test
	public void removeAllContactTest() {
		ResponseEntity<?> expected = ResponseEntity.accepted().build();
		ResponseEntity<?> result = controller.removeAllContacts();
		
		assertEquals(expected, result);
		verify(contactService).removeAll();
	}
}