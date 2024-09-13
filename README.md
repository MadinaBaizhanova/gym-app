# Gym Management Spring Core Application
This project is a Gym Management console application built using Spring Core to manage trainees, trainers, and training sessions. 
The application provides functionalities for creating, updating, deleting, and viewing data for gym members, trainers, and their scheduled training sessions. 
It also supports user management, including creating user accounts and deactivating them.

## How It Works
The Gym Management application uses a simple command-line interface to interact with users. 
Users can perform various operations through a menu-driven interface, including:

* Manage Trainees: Add new Trainees, update their details, view them all or by Trainee ID, or remove them from the system.
* Manage Trainers: Register new Trainers, assign training types, update their information, view them all or by Trainer ID.
* Manage Trainings: Schedule Training sessions, find Training details, and list all sessions.
* User Management: List all users and manage user accounts.

## Technologies Used
* Java 17: The core language for the application.
* Spring Core: For managing application configuration and dependency injection.
* Spring Context and Beans: For configuring and managing the application's beans.
* JUnit 5 & Mockito: For unit testing.
* Lombok: To reduce boilerplate code.
* SLF4J: For logging purposes.
* JaCoCo: For code coverage reporting.
* Maven: For project management and build automation.

The data is stored in a simulated in-memory database (using Java Maps).