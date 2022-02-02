package com.qa.contactbookapi.configuration;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.qa.contactbookapi.data.entity.Contact;
import com.qa.contactbookapi.data.repository.ContactRepository;

@Profile("dev")
@Configuration
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

	private ContactRepository contactRepository;
	
	@Autowired
	public ApplicationStartupListener(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}
	
	// Populate the repository with three dummy contacts when the application starts up.
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		contactRepository.saveAll(List.of(
				new Contact("Kate", "Beckett", "07777777777", "kate.beckett@mycoolmail.com", LocalDate.of(1993, 4, 6)),
				new Contact("Richard", "Castle", "07777777767", "richard.castle@mygreatmail.com", LocalDate.of(1992, 3, 5)),
				new Contact("Kevin", "Ryan", "07777777757", "kevin.ryan@mygoodmail.com", LocalDate.of(1991, 2, 4))
		));
	}

}
