package com.qa.contactbookapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.service.ContactService;

@RestController
@RequestMapping(path = "/contact")
public class ContactController {
	
	private ContactService contactService;
	
	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

    /**
     * Fetch all contacts
     * @return
     * List of contacts in the repository
     */
	@GetMapping
	public ResponseEntity<List<Contact>> fetchContacts() {
		ResponseEntity<List<Contact>> contacts = ResponseEntity.ok(contactService.fetchAll());									 
		return contacts;
	}

    /**
     * Fetch contact based on their ID
     * @param	id	An input ID number (long)
     * @return		The contact fetched from the repository corresponding to the input ID
     */
	@GetMapping(path = "/{id}")
	public ResponseEntity<Contact> fetchContactById(@PathVariable("id") long id) {
		ResponseEntity<Contact> response = ResponseEntity.ok(contactService.fetchById(id));
		return response;
	}

    /**
     * Fetch contact based on their last and first names
     * @param	lastName	The last name (string) of the contact to fetch
     * @param	firstName	The first name (string) of the contact to fetch
     * @return				The contact fetched from the repository corresponding to the input last and first names
     */
	@GetMapping(path = "/{firstName}" + "/{lastName}")
	public ResponseEntity<Contact> fetchContactByLastNameAndFirstName(@PathVariable("lastName") String lastName,
			@PathVariable("firstName") String firstName) {		
		ResponseEntity<Contact> response = ResponseEntity.ok(contactService.fetchByLastNameAndFirstName(lastName, firstName));
		return response;
	}
	
    /**
     * Generate contact
     * @param	contact	The contact object to be generated
     * @return			The contact generated
     */
	@PostMapping
	public ResponseEntity<Contact> generateContact(@Valid @RequestBody Contact contact) {
		
		Contact savedContact = contactService.generate(contact);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + String.valueOf(savedContact.getId()));
		
		ResponseEntity<Contact> response = new ResponseEntity<Contact>(savedContact, headers, HttpStatus.CREATED);
		return response;
	}

    /**
     * Edit contact's details (except for the ID) based on their ID
     * @param	id	An input ID number (long)
     * @return		The edited contact
     */
	@PutMapping("/{id}")
	public ResponseEntity<Contact> editContactById(@PathVariable("id") long id, @Valid @RequestBody Contact contact) {
		
		Contact updatedContact = contactService.editById(id, contact);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + String.valueOf(updatedContact.getId()));
		
		return new ResponseEntity<Contact>(updatedContact, headers, HttpStatus.ACCEPTED);
	}
	
    /**
     * Edit contact's details (except for the ID, and the last and first names) based on their last and first names
     * @param	lastName	The last name (string) of the contact to edit
     * @param	firstName	The first name (string) of the contact to edit
     * @return				The edited contact
     */
	@PutMapping(path = "/{firstName}" + "/{lastName}")
	public ResponseEntity<Contact> editContactByLastNameAndFirstName(@PathVariable("lastName") String lastName,
			@PathVariable("firstName") String firstName, @Valid @RequestBody Contact contact) {

		Contact updatedContact = contactService.editByLastNameAndFirstName(lastName, firstName, contact);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/contact/" + updatedContact.getFirstName() + "/" + updatedContact.getLastName());
		
		return new ResponseEntity<Contact>(updatedContact, headers, HttpStatus.ACCEPTED);
	}
	
    /**
     * Remove contact based on their ID
     * @param	id	An input ID number (long)
     */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeContact(@PathVariable("id") long id) {
		contactService.remove(id);
		return ResponseEntity.accepted().build();
	}

    /**
     * Remove all contacts
     */
	@DeleteMapping
	public ResponseEntity<?> removeAllContacts() {
		contactService.removeAll();
		return ResponseEntity.accepted().build();
	}
		
}
