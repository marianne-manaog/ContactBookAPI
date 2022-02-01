package com.qa.contactbookapi.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.qa.contactbookapi.data.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	boolean existsByLastNameAndFirstName(String lastName, String firstName);

	Contact findByLastNameAndFirstName(String lastName, String firstName);
	
}
