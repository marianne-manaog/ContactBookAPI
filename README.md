# ContactBookAPI

This repository implements a contact book Spring Boot API as a final project for QA Ltd.

###	Dependencies

Please use the pom.xml file in the root directory to install the required dependencies to run this Java 11-based contact book application and consume it as a Spring Boot API.

### How to run it

Further to cloning this repository, this contact book application can be run via either of the following methods: 

1) Programmatically, via an IDE;

2) By running the .jar executable file provided in the root directory of this repository (ContactBookAPI-0.0.1-SNAPSHOT.jar).

Please install the following software tools to be able to run this contact book application and consume it as an API:

- An IDE, e.g., Eclipse;
- Either a plugin for Eclipse with SpringToolSuite4 or its standalone version;
- A tool to test the API, such as Postman.

###	Why are we doing this?

This is the final project of the "Digital Skills Bootcamp in Software Development" at QA Ltd and it aims to assess the required theoretical and practical knowledge in Software Development as per the SFIA Level 2 standards. In particular, this project seeks to assess the following learning objectives:

●	Agile & Project Management (Git, Jira);

●	Databases & Cloud Fundamentals (H2, MySQL);

●	Programming Fundamentals (Java);

●	API Development (Spring Boot);

●	Automated Testing (JUnit).

The main deliverable expected of this project is to develop a fully CRUD functional API, capable of handling HTTP requests from an industry-standard tool, such as Postman. Evidence is expected to be provided to show that data are persisted to the database via the H2 console.

###	How I expected the challenge to go.

Being a novice in Java programming, I was expecting this project to be challenging, particularly as it required to build a full API. The learning curve regarding Spring Boot and the use of automated testing via JUnit was expected to be quite steep; nevertheless, I was hoping that the lessons learnt throughout the bootcamp and the clarifications provided by the tutors, as well as the collaboration with the team prior to this individual project, would have eased the journey.

###	What went well? / What didn't go as planned?

Thinking like a user when planning the stories and tasks on Jira to achieve the above-mentioned objectives in the section named 'Why are we doing this?' was pivotal to ensure the success of this project, by design, regardless of the learning curve involved and some technical hindrances faced when building the application as a .jar file for instance. Overall, I was able to gain the required understanding of how the main components in the application, i.e., the database, the repository, the service, and the rest controller, fit together to provide a fully CRUD-adherent API to consume. This understanding fuelled my learning and accelerated the process of gaining the hands-on skills to implement the plan into Java codes iteratively, via an Agile mentality and way of working.

Writing comprehensive tests (97.2% overall test coverage) to cover all possible (positive and negative) test cases I could think of across the various layers of the application (e.g., repository/data layer; service/business logic layer, controller towards the presentation layer, although with a UI) took longer than expected; however, it was very useful to implement them to understand each component and associated exceptions that may arise at a deeper level from a user perspective, which is essential to ensure the success and adoption of a software-based application.

###	Possible improvements for future revisions of the project.

Further exceptions could be added, especially regarding the custom queries implemented to extend the JPA repository in the contact/custom repository, such as handling invalid/not found contacts/custom objects when leveraging the method 'fetchByLastNameAndFirstName' in the class 'ContactService', which is using the custom query 'findByLastNameAndFirstName'.

More importantly, the data transfer object (DTO) design pattern could be leveraged to enhance the security of the data in the application.

###	Screenshots showing Postman requests and the output from the API.

The full CRUD functionalities implemented were tested via Postman requests as well and the outputs from the API are shown in the screenshots below as related evidence.

#### Create (C) functionality

- Output when creating a contact (having three contacts in the DB beforehand):

![Creating a contact](docs/postman_api_screenshots/create_new_contact.png)

- Output when trying to create a duplicate contact (already created above) and thus throwing the expected duplicate contact-related exception:

![Cannot create a duplicate contact](docs/postman_api_screenshots/cannot_create_duplicate_contact.png)

#### Read (R) functionality

- Output when getting all contacts:

![Getting all contacts](docs/postman_api_screenshots/get_all_contacts.png)

- Output when getting a contact by ID:

![Getting a contact by ID](docs/postman_api_screenshots/get_contact_by_id.png)

- Output when getting a contact by first and last names:

![Getting a contact by first and last names](docs/postman_api_screenshots/get_contact_by_first_and_last_names.png)

#### Update (U) functionality

- Output when editing a contact by ID:

![Editing a contact by ID](docs/postman_api_screenshots/edit_contact_by_id.png)

- Output when trying to edit a contact whose ID does not exist in the DB and thus throwing the expected invalid/not found contact-related exception:

![Cannot edit a contact with non-existent ID](docs/postman_api_screenshots/cannot_edit_contact_with_non_existent_id.png)

- Output when editing a contact by first and last names:

![Editing a contact by first and last names](docs/postman_api_screenshots/edit_contact_by_first_and_last_names.png)

- Output when trying to edit a contact whose first and last names do not exist in the DB and thus throwing the expected invalid/not found contact-related exception:

![Cannot edit a contact with non-existent first and last names](docs/postman_api_screenshots/cannot_edit_contact_by_non_existent_first_and_last_names.png)

#### Delete (D) functionality

- Output when deleting a contact by ID:

![Deleting contact by ID](docs/postman_api_screenshots/delete_contact_by_id.png)

- Output confirming the deletion of a contact by ID:

![Confirming deletion of contact by ID](docs/postman_api_screenshots/confirm_deletion_of_first_contact.png)

- Output when deleting all contacts:

![Deleting all contacts](docs/postman_api_screenshots/delete_all_contacts.png)

- Output confirming the deletion of all contacts:

![Confirming deletion of all contacts](docs/postman_api_screenshots/confirm_deletion_of_all_contacts.png)

###	Screenshots of database to prove that data are being persisted.

The screenshots of the H2 database further to the above-mentioned API requests are provided below to confirm that data are persisted correctly.

#### Confirmation of data persistence further to Create (C) functionality

- Confirming the creation of an additional (fourth) contact in the DB:

![Confirming the creation of an additional (fourth) contact in the DB](docs/h2_db_contacts_persistence_following_api_requests/confirming_creation_of_fourth_contact.png)

#### Confirmation of data persistence further to Read (R) functionality

- Confirming that the initial three contacts are persisted in the DB and thus can be read from it:

![Confirming that the initial three contacts are persisted in the DB](docs/h2_db_contacts_persistence_following_api_requests/showing_initial_db_with_three_contacts.png)

#### Confirmations of data persistence further to Update (U) functionality

- Confirming that the first contact has been updated by ID and persisted in the DB:

![Confirming that the first contact has been updated by ID and persisted in the DB](docs/h2_db_contacts_persistence_following_api_requests/confirming_first_contact_edited_by_id.png)

- Confirming that the first contact has been updated by their first and last names and persisted in the DB:

![Confirming that the first contact has been updated by their first and last names and persisted in the DB](docs/h2_db_contacts_persistence_following_api_requests/confirming_first_contact_edited_by_first_and_last_names.png)

#### Confirmations of data persistence further to Delete (D) functionality

- Confirming the deletion of the first contact from the DB by ID:

![Confirming the deletion of the first contact from the DB by ID](docs/h2_db_contacts_persistence_following_api_requests/confirming_deletion_of_first_contact_by_id.png)

- Confirming the deletion of all contacts from the DB:

![Confirming the deletion of all contacts from the DB](docs/h2_db_contacts_persistence_following_api_requests/confirming_deletion_of_all_contacts.png)

###	Screenshot of test results, including coverage report.

The screenshot of the unit and integration test results (all 43 tests passed), along with the full coverage report indicating 97.2% test coverage, is provided below. 

![Test results and coverage](docs/Test_results_with_coverage_report.png)

###	Link to Jira Board

The work is tracked on Jira [at this link](https://marianne.atlassian.net/jira/software/projects/DFP/boards/2).
