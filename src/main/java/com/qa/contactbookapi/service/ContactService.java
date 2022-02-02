package com.qa.contactbookapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.data.repository.ContactRepository;
import com.qa.contactbookapi.exceptions.DuplicateContactException;
import com.qa.contactbookapi.exceptions.InvalidContactException;

@Service
public class ContactService {
	
	private ContactRepository contactRepository;
	
	@Autowired
	public ContactService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	public List<Contact> fetchAll() {
		return contactRepository.findAll();
	}

    /**
     * Fetch contact based on their ID
     * @param	id	An input ID number (long)
     * @return		The contact fetched from the repository corresponding to the input ID	
     * @throws	InvalidContactException
     * If the ID were not found in the repository, it would throw this exception.
     */
	public Contact fetchById(long id) {
		return contactRepository.findById(id).orElseThrow(() -> {
			return new InvalidContactException("Cannot find contact with ID " + id);
		});
	}
	
    /**
     * Fetch contact based on their last and first names
     * @param	lastName	The last name (string) of the contact to fetch
     * @param	firstName	The first name (string) of the contact to fetch
     * @return				The contact fetched from the repository corresponding to the input last and first names
     */
	public Contact fetchByLastNameAndFirstName(String lastName, String firstName) {
		return contactRepository.findByLastNameAndFirstName(lastName, firstName);
	}

    /**
     * Generate contact if unique
     * @param	contact	The contact object to be generated
     * @return			The contact generated
     * @throws	DuplicateContactException
     * If the contact were already in the repository, it would throw this exception.
     */
	public Contact generate(Contact contact) {
		
		// 1. Check if the contact were found based on their last and first names
		if (!contactRepository.existsByLastNameAndFirstName(contact.getLastName(), contact.getFirstName())) {
			
			// 2. Create new contact if unique (based on their last and first names)
			Contact persistedContact = contactRepository.save(contact);
			return persistedContact;
				
		} else {
			throw new DuplicateContactException("Contact " + contact + " is a duplicate");
		}

	}
	
    /**
     * Edit contact's details (except for the ID) based on their ID
     * @param	id	An input ID number (long)
     * @return		The edited contact
     * @throws	InvalidContactException
     * If the ID were not found in the repository, it would throw this exception.
     */
	public Contact editById(long id, Contact contact) {
		

		if (contactRepository.existsById(id)) {

			Contact contactToEdit = contactRepository.getById(id);
			
			contactToEdit.setFirstName(contact.getFirstName());
			contactToEdit.setLastName(contact.getLastName());
			contactToEdit.setMobileNumber(contact.getMobileNumber());
			contactToEdit.setEmailAddress(contact.getEmailAddress());
			contactToEdit.setDateOfBirth(contact.getDateOfBirth());
			
			return contactRepository.save(contactToEdit);
		} else {
			throw new InvalidContactException("Cannot edit contact with ID " + id);
		}
	}
	
    /**
     * Edit contact's details (except for the ID, and the last and first names) based on their last and first names
     * @param	lastName	The last name (string) of the contact to edit
     * @param	firstName	The first name (string) of the contact to edit
     * @return				The edited contact
     * @throws	InvalidContactException
     * If the ID were not found in the repository, it would throw this exception.
     */	
	public Contact editByLastNameAndFirstName(String lastName, String firstName, Contact contact) {
		
		if (contactRepository.existsByLastNameAndFirstName(lastName, firstName)) {
			
			Contact contactToEdit = contactRepository.findByLastNameAndFirstName(lastName, firstName);
			
			contactToEdit.setMobileNumber(contact.getMobileNumber());
			contactToEdit.setEmailAddress(contact.getEmailAddress());
			contactToEdit.setDateOfBirth(contact.getDateOfBirth());
		
			return contactRepository.save(contactToEdit);
		} else {
			throw new InvalidContactException("Cannot edit contact with first name " + firstName + " and last name " + lastName);
		}
	}

    /**
     * Remove contact based on their ID
     * @param	id	An input ID number (long) corresponding to the contact to be removed	
     * @throws	InvalidContactException
     * If the ID were not found in the repository, it would throw this exception.
     */
	public void remove(long id) {
		if (contactRepository.existsById(id)) {
			contactRepository.deleteById(id);
		} else {
			throw new InvalidContactException("Cannot remove non-existent contact with ID " + id);
		}
	}
	
	public void removeAll() {
		contactRepository.deleteAllInBatch();
	}

}
