package com.qa.contactbookapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.data.repository.ContactRepository;
import com.qa.contactbookapi.exceptions.InvalidContactException;


@ExtendWith(MockitoExtension.class)
public class ContactServiceUnitTest {

	@Mock
	private ContactRepository contactRepository;

	@InjectMocks
	private ContactService contactService;

	private List<Contact> contactsList;
	private Contact expectedContactHavingId;
	private Contact expectedContactNotHavingId;

	@BeforeEach
	public void init() {
		contactsList = new ArrayList<>();
		contactsList.addAll(List.of(
				new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact(2L, "Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact(3L, "Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))
		));
		expectedContactNotHavingId = new Contact("Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
		expectedContactHavingId = new Contact(4L, "Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
	}

	@Test
	public void fetchAllContactsTest() {
		when(contactRepository.findAll()).thenReturn(contactsList);
		assertThat(contactService.fetchAll()).isEqualTo(contactsList);
		verify(contactRepository).findAll();
	}

	@Test
	public void fetchContactByIdTest() {
		long id = expectedContactHavingId.getId();
		when(contactRepository.findById(id)).thenReturn(Optional.of(expectedContactHavingId));
		assertThat(contactService.fetchById(id)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).findById(id);
	}
	
	@Test
	public void fetchContactByInvalidIdTest() {

		long id = 55;
		when(contactRepository.findById(id)).thenReturn(Optional.empty());
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.fetchById(id);
		});
		
		String expected = "Cannot find contact with ID " + id;
		assertThat(e.getMessage()).isEqualTo(expected);
		verify(contactRepository).findById(id);
	}
	
	@Test
	public void fetchContactByLastNameAndFirstNameTest() {
		String firstName = expectedContactHavingId.getFirstName();
		String lastName = expectedContactHavingId.getLastName();
		when(contactRepository.findByLastNameAndFirstName(lastName, firstName)).thenReturn(expectedContactHavingId);
		assertThat(contactService.fetchByLastNameAndFirstName(lastName, firstName)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).findByLastNameAndFirstName(lastName, firstName);
	}
	
	@Test
	public void generateContactTest() {
		when(contactRepository.save(expectedContactNotHavingId)).thenReturn(expectedContactHavingId);

		assertThat(contactService.generate(expectedContactNotHavingId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).save(expectedContactNotHavingId);
	}

	@Test
	public void editContactByIdTest() {

		long id = expectedContactHavingId.getId();
		Contact contactToEdit = new Contact(expectedContactHavingId.getId(),
											  expectedContactHavingId.getFirstName(), 
											  expectedContactHavingId.getLastName(),
											  expectedContactHavingId.getMobileNumber(),
											  expectedContactHavingId.getEmailAddress(),
											  expectedContactHavingId.getDateOfBirth());
		
		when(contactRepository.existsById(id)).thenReturn(true);
		when(contactRepository.getById(id)).thenReturn(expectedContactHavingId);
		when(contactRepository.save(expectedContactHavingId)).thenReturn(contactToEdit);
		
		assertThat(contactService.editById(id, contactToEdit)).isEqualTo(contactToEdit);
		verify(contactRepository).existsById(id);
		verify(contactRepository).getById(id);
		verify(contactRepository).save(expectedContactHavingId);
	}

	@Test
	public void editContactByLastNameAndFirstName() {

		String firstName = expectedContactHavingId.getFirstName();
		String lastName = expectedContactHavingId.getLastName();
		
		Contact contactToEdit = new Contact(expectedContactHavingId.getId(),
											  expectedContactHavingId.getFirstName(), 
											  expectedContactHavingId.getLastName(),
											  expectedContactHavingId.getMobileNumber(),
											  expectedContactHavingId.getEmailAddress(),
											  expectedContactHavingId.getDateOfBirth());
		
		when(contactRepository.existsByLastNameAndFirstName(lastName, firstName)).thenReturn(true);
		when(contactRepository.findByLastNameAndFirstName(lastName, firstName)).thenReturn(expectedContactHavingId);
		when(contactRepository.save(expectedContactHavingId)).thenReturn(contactToEdit);
		
		assertThat(contactService.editByLastNameAndFirstName(lastName, firstName, contactToEdit)).isEqualTo(contactToEdit);
		verify(contactRepository).existsByLastNameAndFirstName(lastName, firstName);
		verify(contactRepository).findByLastNameAndFirstName(lastName, firstName);
		verify(contactRepository).save(expectedContactHavingId);
	}
	
	@Test
	public void removeContactTest() {
		long id = expectedContactHavingId.getId();
		when(contactRepository.existsById(id)).thenReturn(true);
		contactService.remove(id);
		verify(contactRepository).existsById(id);
		verify(contactRepository).deleteById(id);
	
	}
		
	@Test
	public void removeAllContactsTest() {
		List<Contact> emptyList = new ArrayList<Contact>();
		contactService.removeAll();
		assertEquals(contactRepository.findAll(), emptyList);
	}
	
}
