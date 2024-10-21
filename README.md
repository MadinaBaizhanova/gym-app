﻿# Gym Management Spring Core Application
This project is a Gym Management console application built using Spring Core to manage trainees, trainers, and training sessions. 
The application provides functionalities for managing gym members, trainers, and their scheduled training sessions. 

## How It Works
The Gym Management application uses a simple command-line interface to interact with users. 
Users can perform various operations through a menu-driven interface, including:

##### MANAGE TRAINEES:
* View trainee account information
* Update trainee profile (first name, last name, address, and date of birth)
* Delete trainee profile
* View all available trainers and assigned favorite trainers
* Add or remove trainers to/from the favorite list
* View scheduled training sessions
* Schedule a new training session

##### MANAGE TRAINERS:
* View trainer account information
* Update trainer profile (first name, last name, training type)
* View scheduled training sessions

##### MANAGE TRAININGS:
* Schedule training sessions
* View training details by filtering through trainers, training types, and date ranges
* List all scheduled sessions

##### USER MANAGEMENT:
* Register as a new trainee or trainer
* Log in as a trainee or trainer
* Activate or deactivate a user profile
* Change user password
* Log out from the system

##### TECHNOLOGIES USED
* Java 17: The core language for the application.
* Spring Core: For managing application configuration and dependency injection.
* Spring Context and Beans: For configuring and managing the application's beans.
* PostgreSQL: The database used for persisting user, trainee, trainer, and training data. 
* Hibernate: For ORM (Object Relational Mapping) and database interaction.
* JUnit 5 & Mockito: For unit testing.
* Lombok: To reduce boilerplate code.
* SLF4J: For logging purposes.
* JaCoCo: For code coverage reporting.
* Maven: For project management and build automation.
* 
