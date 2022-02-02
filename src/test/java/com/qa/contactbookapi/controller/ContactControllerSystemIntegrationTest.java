package com.qa.contactbookapi.controller;

import com.qa.contactbookapi.data.entity.Contact;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class ContactControllerSystemIntegrationTest {

	@Mock
	ContactController contactController;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private List<Contact> expectedContactsList;
	
	private Contact expectedFirstContact;
	
	private Long firstContactId;
	
	@BeforeEach
	public void init() {
		
		firstContactId = 1L;
		Long secondContactId = 2L;
		Long thirdContactId = 3L;
		
		expectedContactsList = List.of(
				new Contact(firstContactId, "Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact(secondContactId, "Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact(thirdContactId, "Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))
		);
		
		expectedFirstContact = expectedContactsList.get(0);
	}
	
	@Test
	public void fetchAllContactsTest() throws Exception {

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/contact/");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(expectedContactsList));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactsJson = objectMapper.writeValueAsString(expectedContactsList);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactsJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void fetchContactByIdTest() throws Exception {
				
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/contact/" + firstContactId);
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(firstContactId));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactJson = objectMapper.writeValueAsString(expectedFirstContact);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void fetchContactByLastNameAndFirstNameTest() throws Exception {
		
		String firstNameExpectedContact = expectedFirstContact.getFirstName();
		String lastNameExpectedContact = expectedFirstContact.getLastName();
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/contact/" + firstNameExpectedContact + "/" + lastNameExpectedContact);
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(firstNameExpectedContact));
		mockRequest.content(objectMapper.writeValueAsString(lastNameExpectedContact));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactJson = objectMapper.writeValueAsString(expectedFirstContact);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
	@Test
	public void generateContactTest() throws Exception {

		Long contactId = 4L;
		
		Contact contactToPersist = new Contact("Katie", "Brockett", "08777777777", "katie.brockett@mycoolmail.com", LocalDate.of(1983, 2, 1));
		Contact expectedContact = new Contact(contactId, "Katie", "Brockett", "08777777777", "katie.brockett@mycoolmail.com", LocalDate.of(1983, 2, 1));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/contact");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(contactToPersist));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactJson = objectMapper.writeValueAsString(expectedContact);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void editContactByIdTest() throws Exception {
		
		Contact editedContact = new Contact("Katie", "Backett", "07777777777", "katie.backett@mycoolmail.com", LocalDate.of(1992, 4, 6));
		Contact expectedContact = new Contact(firstContactId, "Katie", "Backett", "07777777777", "katie.backett@mycoolmail.com", LocalDate.of(1992, 4, 6));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/contact/" + firstContactId);
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(editedContact));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactJson = objectMapper.writeValueAsString(expectedContact);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}

	@Test
	public void editContactByLastNameAndFirstNameTest() throws Exception {
		
		Contact editedContact = new Contact("Kate", "Beckett", "07777777778", "katie.backett@mycoolmail.com", LocalDate.of(1992, 4, 6));
		Contact expectedContact = new Contact(firstContactId, "Kate", "Beckett", "07777777778", "katie.backett@mycoolmail.com", LocalDate.of(1992, 4, 6));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/contact/" + editedContact.getFirstName() + "/" + editedContact.getLastName());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(editedContact));
		mockRequest.accept(MediaType.APPLICATION_JSON);

		String contactJson = objectMapper.writeValueAsString(expectedContact);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content().json(contactJson);

		mockMvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
	}
	
    @Test
    public void removeContactTest() throws Exception {
        Mockito.when(contactController.removeContact(firstContactId)).thenReturn(ResponseEntity.accepted().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/contact", firstContactId)).andExpect(status().isAccepted());
    }
	
    @Test
    public void removeAllContactsTest() throws Exception {  
        Mockito.when(contactController.removeAllContacts()).thenReturn(ResponseEntity.accepted().build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/contact")).andExpect(status().isAccepted());
    }
    
}
