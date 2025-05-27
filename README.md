# iGym
iGym is a mobile-first website designed to help users track and manage their gym workouts. The app stores all essential workout information, allowing users to focus on training without worrying about workout routines or weights.

## Features
- Organize and track your daily workouts.
- Record weights, repetitions, and other workout details.
- Seamlessly access your workout data from anywhere.
- Customize workouts for each gym: Users can register different workout routines for each gym they attend, allowing for adaptations based on the specific equipment available.

## Deployed version:
You can access the deployed application [here](http://100.29.52.128/). You're welcome to check it out, sign up, and explore the features we’ve built!

---

## Technologies
The iGym project is built using:

### Backend
- **Java**: 21.0.5
- **PostgreSQL**: 17.2
- **Spring Boot**: 3.4.1
- **JPA (Jakarta Persistence API)**
- **Maven**: 3.9.9
- **Mockito**
- **SonarQube**
- **JWT (JSON Web Tokens)**

### Frontend
- **React**
- **Vite**
- **Node.js**
- **Material-UI (MUI)**
- **Axios**

> Note: Spring Boot and JPA are managed automatically by Maven; no separate installation is required.

---

## Getting Started

Follow these steps to set up the iGym project on your local machine and start contributing:

### Prerequisites
1. Install **Java Development Kit (JDK) 21.0.5**.
2. Configure the environment variable `JAVA_HOME` to point to your JDK installation.
3. Install **PostgreSQL 17.2**.
4. Install **Maven 3.9.9**
5. Install **Node.js** (Latest LTS version recommended)

### Environment Variables Setup
#### Server

Set up the following environment variables in a .env file inside server directory:

For Windows PowerShell create env.ps1 file with the content:
```powershell
$env:DB_URL="your_db_url"
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
$env:SERVER_PORT="your_server_port"
$env:JWT_SECRET="your_jwt_secret_key"
$env:JWT_EXPIRATION="your_jwt_expiration"
```

For Linux/MacOS create env.sh file with the content:
```bash
export DB_URL="your_db_url"
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"
export SERVER_PORT="your_server_port"
export JWT_SECRET="your_jwt_secret_key"
export JWT_EXPIRATION="your_jwt_expiration"
```

> Note: Replace the fields with your actual values. Never commit these values to version control.

#### Client
Update VITE_API_BASE variable that is defined in client/.env.development with the same port as the server

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
3. Update the database credentials in the .env file as required. Remember to not commit this changes.

### Running the Application

#### Server
To start the server application:
```bash
# Navigate to the server directory
cd server

# Load environemntal variables
. ./env.ps1 #powershel
source ./env.sh #Linux/MacOS

# Start the application
mvn spring-boot:run
```

#### Client
To start the client application:
```bash
# Navigate to the client directory
cd client

# Install dependencies
npm install

# Start the development server
npm run dev
```

The client will be available at `http://localhost:5173` by default, and the server will run on the port specified in your environment variables.

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

## Documentation

The full documentation of the application is available at: [https://tomerpeceniski.github.io/igym/](https://tomerpeceniski.github.io/igym/)


---

## License
This project is open-source. Feel free to use, modify, and distribute it in accordance with the applicable license.
