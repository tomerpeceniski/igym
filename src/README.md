# iGym
iGym is a mobile-first website designed to help users track and manage their gym workouts. The app stores all essential workout information, allowing users to focus on training without worrying about workout routines or weights.

## Features
- Organize and track your daily workouts.
- Record weights, repetitions, and other workout details.
- Seamlessly access your workout data from anywhere.
- Customize workouts for each gym: Users can register different workout routines for each gym they attend, allowing for adaptations based on the specific equipment available.

---

## Technologies
The iGym project is built using:
- **Java**: 21.0.5
- **PostgreSQL**: 17.2
- **Spring Boot**: 3.4.1
- **JPA (Jakarta Persistence API)**
- **Maven**: 3.9.9
- **Mockito**
- **SonarQube**

> Note: Spring Boot and JPA are managed automatically by Maven; no separate installation is required.

---

## Getting Started

Follow these steps to set up the iGym project on your local machine and start contributing:

### Prerequisites
1. Install **Java Development Kit (JDK) 21.0.5**.
2. Configure the environment variable `JAVA_HOME` to point to your JDK installation.
3. Install **PostgreSQL 17.2**.
4. Install **Maven 3.9.9**

### Database Setup
To set up the PostgreSQL database, follow these steps:

1. Open the PostgreSQL CLI or your preferred database management tool.
2. Create the igym_db database with the following SQL command:
   ```sql
   CREATE DATABASE igym_db
   WITH OWNER postgres
   ENCODING 'UTF8'
   LC_COLLATE = 'en_US.UTF-8'
   LC_CTYPE = 'en_US.UTF-8'
   CONNECTION LIMIT = -1;
   ```
3. Update the database credentials in the application.properties file as required. Remember to not commit this changes.
    ```
    spring.datasource.url=jdbc:postgresql://localhost:5432/igym_db
    spring.datasource.username=your_database_username
    spring.datasource.password=your_database_password
    spring.jpa.hibernate.ddl-auto=update
    ```

### Running the Application
To start the application, use the following command:
```bash
# Start the application
mvn spring-boot:run
```

### Testing the Application
To run unit tests:
```bash
# Execute tests
mvn test
```

## Contributing
We welcome contributions to iGym! Here are a few ways you can contribute:
- Report bugs or suggest new features.
- Create pull requests with enhancements or fixes.
- Help improve the project documentation.

### Contact
If you have any questions or need help setting up the project, feel free to reach out. Let's build iGym together! 💪

---

## License
This project is open-source. Feel free to use, modify, and distribute it in accordance with the applicable license.
