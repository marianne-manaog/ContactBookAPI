package com.qa.contactbookapi.data.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ContactTest {

	private Contact firstContact;
	private Contact firstContactCopy;
	private Contact firstContactWithFirstAndLastNamesInverted;
	
	@BeforeEach
	public void init() {
		firstContact = new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6));
		
		firstContactCopy = new Contact(1L, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6));
		
		firstContactWithFirstAndLastNamesInverted = new Contact(1L, "Beckett", "Kate", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6));
	}

	@Test
	public void hashCodePositiveTest() {
		assertEquals(firstContact, firstContactCopy);
		assertTrue(firstContact.hashCode()==firstContactCopy.hashCode());
	
	}
	
	@Test
	public void hashCodeNegativeTest() {
		assertTrue(firstContact.hashCode()!=firstContactWithFirstAndLastNamesInverted.hashCode());
	
	}
	
    @Test
    public void equalPositiveTest() {
        assertTrue(firstContact.equals(firstContactCopy));
    }
    
    @Test
    public void equalNegativeTest() {
        assertFalse(firstContact.equals(firstContactWithFirstAndLastNamesInverted));
    }
	
}
