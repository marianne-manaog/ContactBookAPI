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

import com.qa.contactbookapi.exceptions.DuplicateContactException;
import com.qa.contactbookapi.exceptions.InvalidContactException;


@ExtendWith(MockitoExtension.class)
public class ContactServiceUnitTest {

	@Mock
	private ContactRepository contactRepository;

	@InjectMocks
	private ContactService contactService;

	private List<Contact> contactsList;
	
	private long expectedContactId;
	
	private String firstNameExpectedContactHavingId;
	private String lastNameExpectedContactHavingId;
	
	private long nonExistentId;
	
	private Contact duplicateFirstContactNotHavingId;
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
		
		expectedContactHavingId = new Contact(4L, "Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
		expectedContactId = expectedContactHavingId.getId();
		
		firstNameExpectedContactHavingId = expectedContactHavingId.getFirstName();
		lastNameExpectedContactHavingId = expectedContactHavingId.getLastName();
		
		nonExistentId = 55;
		
	}

	@Test
	public void fetchAllContactsTest() {
		when(contactRepository.findAll()).thenReturn(contactsList);
		assertThat(contactService.fetchAll()).isEqualTo(contactsList);
		verify(contactRepository).findAll();
	}

	@Test
	public void fetchContactByValidIdTest() {
		when(contactRepository.findById(expectedContactId)).thenReturn(Optional.of(expectedContactHavingId));
		assertThat(contactService.fetchById(expectedContactId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).findById(expectedContactId);
	}
	
	@Test
	public void fetchContactByInvalidIdTest() {

		when(contactRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.fetchById(nonExistentId);
		});
		
		String expected = "Cannot find contact with ID " + nonExistentId;
		assertThat(e.getMessage()).isEqualTo(expected);
	}
	
	@Test
	public void fetchContactByLastNameAndFirstNameTest() {
		when(contactRepository.findByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId)).thenReturn(expectedContactHavingId);
		assertThat(contactService.fetchByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).findByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId);
	}
	
	@Test
	public void generateContactTest() {
		
		expectedContactNotHavingId = new Contact("Richard", "Feynman", "07777777757", "rick.feynman@myquantummail.com", LocalDate.of(1960, 1, 1));
		
		when(contactRepository.save(expectedContactNotHavingId)).thenReturn(expectedContactHavingId);

		assertThat(contactService.generate(expectedContactNotHavingId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).save(expectedContactNotHavingId);
	}
	
	@Test
	public void generateContactDuplicateExceptionTest() {

		duplicateFirstContactNotHavingId = new Contact("Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6));
		String duplicateLastName = duplicateFirstContactNotHavingId.getLastName();
		String duplicateFirstName = duplicateFirstContactNotHavingId.getFirstName();
		
		when (contactRepository.existsByLastNameAndFirstName(duplicateLastName, duplicateFirstName)).thenReturn(true);
		
		DuplicateContactException e = Assertions.assertThrows(DuplicateContactException.class, () -> {
			contactService.generate(duplicateFirstContactNotHavingId);
		});
		
		String expected = "Contact " + duplicateFirstContactNotHavingId + " is a duplicate";
		assertThat(e.getMessage()).isEqualTo(expected);
	}
	
	@Test
	public void editContactByValidIdTest() {
		
		when(contactRepository.existsById(expectedContactId)).thenReturn(true);
		when(contactRepository.getById(expectedContactId)).thenReturn(expectedContactHavingId);
		when(contactRepository.save(expectedContactHavingId)).thenReturn(expectedContactHavingId);
		
		assertThat(contactService.editById(expectedContactId, expectedContactHavingId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).existsById(expectedContactId);
		verify(contactRepository).getById(expectedContactId);
		verify(contactRepository).save(expectedContactHavingId);
	}

	@Test
	public void editContactByInvalidIdTest() {
		
		Contact contactToEdit = new Contact(
				nonExistentId,
				firstNameExpectedContactHavingId,
				lastNameExpectedContactHavingId,
				expectedContactHavingId.getMobileNumber(),
				expectedContactHavingId.getEmailAddress(),
				expectedContactHavingId.getDateOfBirth());
		
		when(contactRepository.existsById(nonExistentId)).thenReturn(false);
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.editById(nonExistentId, contactToEdit);
		});
		
		String expected = "Cannot edit contact with ID " + nonExistentId;
		assertThat(e.getMessage()).isEqualTo(expected);
	}
	
	@Test
	public void editContactByValidLastNameAndFirstName() {

		when(contactRepository.existsByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId)).thenReturn(true);
		when(contactRepository.findByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId)).thenReturn(expectedContactHavingId);
		when(contactRepository.save(expectedContactHavingId)).thenReturn(expectedContactHavingId);
		
		assertThat(contactService.editByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId, expectedContactHavingId)).isEqualTo(expectedContactHavingId);
		verify(contactRepository).existsByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId);
		verify(contactRepository).findByLastNameAndFirstName(lastNameExpectedContactHavingId, firstNameExpectedContactHavingId);
		verify(contactRepository).save(expectedContactHavingId);
	}

	@Test
	public void editContactByInvalidLastNameAndFirstName() {

		String invalidLastName = "Kripke";
		String invalidFirstName = "Barry";
		
		Contact invalidContactToEdit = new Contact(expectedContactHavingId.getId(),
				invalidFirstName, 
				invalidLastName,
				expectedContactHavingId.getMobileNumber(),
				expectedContactHavingId.getEmailAddress(),
				expectedContactHavingId.getDateOfBirth());
		
		when(contactRepository.existsByLastNameAndFirstName(invalidLastName, invalidFirstName)).thenReturn(false);
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.editByLastNameAndFirstName(invalidLastName, invalidFirstName, invalidContactToEdit);
		});
		
		String expected = "Cannot edit contact with first name " + invalidFirstName + " and last name " + invalidLastName;
		assertThat(e.getMessage()).isEqualTo(expected);
	}
	
	@Test
	public void removeContactValidIdTest() {
		when(contactRepository.existsById(expectedContactId)).thenReturn(true);
		contactService.remove(expectedContactId);
		verify(contactRepository).existsById(expectedContactId);
		verify(contactRepository).deleteById(expectedContactId);
	
	}
	
	@Test
	public void removeContactInvalidIdTest() {

		when(contactRepository.existsById(nonExistentId)).thenReturn(false);
		
		InvalidContactException e = Assertions.assertThrows(InvalidContactException.class, () -> {
			contactService.remove(nonExistentId);
		});
		
		String expected = "Cannot remove non-existent contact with ID " + nonExistentId;
		assertThat(e.getMessage()).isEqualTo(expected);
	}
	
	@Test
	public void removeAllContactsTest() {
		List<Contact> emptyList = new ArrayList<Contact>();
		contactService.removeAll();
		assertEquals(contactRepository.findAll(), emptyList);
	}
	
}
