# Zettel – JavaFX Note-Taking Application

Zettel is a **JavaFX-based note-taking application** that provides an intuitive platform to manage notes, events, and users. It features a clean, multi-tab GUI with real-time Markdown previews, making note-taking seamless and efficient.

## Features
- **Note Management:** Partially implemented. Supports basic note creation and editing across multiple tabs.
- **Event Management:** To be implemented.
- **User Management:** To be implemented.
- **JavaFX GUI:** Responsive, FXML-based graphical interface.
- **JUnit Testing:** Comprehensive unit tests planned for user and event management.
- **Maven Build:** Supports easy dependency management and project setup.

## Technologies Used
- Java
- JavaFX
- Maven
- FXML
- JUnit

## Project Structure
```text
Zettel/
├── src/
│   ├── main/
│   │   ├── java/com/zettel/ (events, gui, notemanager, usermanager)
│   │   └── resources/preview_format/
│   └── test/
│       ├── java/com/zettel/ (parameterized, regression, test)
│       └── resources/
├── target/
├── pom.xml
└── README.md
```


## Setup and Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/sivasurya16/Zettel.git
   ```
2. Import the project into your preferred IDE (IntelliJ, Eclipse, etc.) as a **Maven project**.
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the JavaFX application using:
   ```bash
   mvn clean javafx:run
   ```

## Running Tests
JUnit tests are under development. To run available tests:
```bash
mvn test
```

## To-Do
- [ ] Complete **Note Management** (Add deletion, improved saving, etc.)
- [ ] Implement **Event Management**
- [ ] Implement **User Management**
- [ ] Add comprehensive **JUnit test cases** for all features
- [ ] Improve UI/UX and add additional usability features
- [ ] Add data persistence (optional)

## Screenshots
![{EC8492C0-4853-41FB-9DED-1CA7D690E99D}](https://github.com/user-attachments/assets/8b5ea746-686b-4a86-9809-b2d4332c2b99)

