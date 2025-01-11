# iGym

iGym is a mobile-first website designed to help users keep track of their gym workouts. With iGym, users can head to the gym without worrying about questions like "What's today's workout?" or "How much weight should I use for this exercise?". All the essential information is stored in the app for quick and easy access.

## Features
- Organize and track your daily workouts.
- Record weights, repetitions, and other workout details.
- Seamlessly access your workout data from anywhere.

---

## Technologies
The iGym project is built using:
- **Java**: 21.0.5  
- **PostgreSQL**: 17.2

---

## Getting Started

Follow these steps to set up the iGym project on your local machine and start contributing:

### Prerequisites
1. Install **Java 21.0.5**.
2. Install **PostgreSQL 17.2**.

### Database Setup
1. Create a PostgreSQL user and database:
   ```sql
   CREATE DATABASE igym_db
   WITH OWNER postgres
   ENCODING 'UTF8'
   LC_COLLATE = 'en_US.UTF-8'
   LC_CTYPE = 'en_US.UTF-8'
   CONNECTION LIMIT = -1;
   ```
2. Update your database credentials with the information in the `application.properties` file

### Running the Application
To start the application, use the following command:
```bash
mvn spring-boot:run
```

## Contributing
We welcome contributions to iGym!

Feel free to reach out if you have any questions or need help setting up the project. Let's build iGym together! 💪