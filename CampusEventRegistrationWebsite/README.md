# Campus Event Registration Website

A beginner-friendly website mini project using Java Spring Boot, HTML, CSS, and JavaScript. No MySQL is used. Data is held in memory while the application is running.

## Features
- View sample campus events
- Add events through the organizer form
- Register students for events
- Prevent duplicate registration for the same event and register number
- Prevent registration when event capacity is reached
- View recent registrations

## Requirements
- JDK 17 or newer
- VS Code
- VS Code Extension Pack for Java
- Maven installed and available in PATH (or use the VS Code Maven extension)

## Run in VS Code
1. Extract the ZIP.
2. Open the extracted `CampusEventRegistrationWebsite` folder in VS Code. Open the folder containing `pom.xml`, not just the `src` folder.
3. Wait for VS Code to import the Maven project.
4. Open Terminal > New Terminal.
5. Run: `mvn spring-boot:run`
6. Wait until the terminal says Spring Boot has started.
7. Open a browser and visit: `http://localhost:8080`

## Stop the website
In the VS Code terminal, press `Ctrl + C`.

## Important note about simple storage
This project uses Java memory, not a database or file. Added events and registrations disappear when the server is stopped or restarted. It is suitable for learning and a local demonstration, but not for real production use.

## Troubleshooting
- `mvn is not recognized`: install Apache Maven and add its `bin` folder to PATH, or use the Maven extension in VS Code.
- `JAVA_HOME` or Java version error: install JDK 17 or newer and configure VS Code to use it.
- Port 8080 is already in use: close the other app using it or add `server.port=8081` to `src/main/resources/application.properties`, then open `http://localhost:8081`.
- Website says it cannot load events: check the terminal for startup errors and ensure the browser URL starts with `http://localhost:8080`.
