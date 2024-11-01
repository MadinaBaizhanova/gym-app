# Gym Management Spring Boot application
This project is a Gym Management REST API to manage trainees, trainers, and training sessions. 
The application provides functionalities for managing gym members, trainers, and their scheduled training sessions. 

## How It Works
Users can perform various operations through REST API, including:

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
* View training details by filtering through trainers/trainees, training types, and date ranges

##### USER MANAGEMENT:
* Register as a new trainee or trainer
* Log in as a trainee or trainer
* Activate or deactivate a user profile
* Change user password
* Log out from the system

##### TECHNOLOGIES USED
* Java 17
* Spring Boot
* Spring Security
* Spring Actuator
* PostgreSQL
* Hibernate
* JUnit 5 & Mockito
* Lombok
* SLF4J
* JaCoCo
* Maven