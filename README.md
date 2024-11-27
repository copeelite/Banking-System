# Banking System

A JavaFX-based banking management system that provides customer account management, transaction processing, and other banking functionalities.

## System Requirements
- Maven will be used to build the project
- Java 21 or higher
- Maven 3.8.5 or higher

## Installation Steps

1. Clone the project
2. Database Setup
- SQLite already included in the project
- SQLite database file will be created automatically on first run
- Database schema is defined in:


3. Application Configuration
- Application settings in:

```properties:src/main/resources/app.properties
title=Banking Application
icon=/icon/icon.png
stage.width=600
stage.height=400
stage.resizable=true
```

4. Build the project
```bash
mvn clean install
```

5. Run the application
```bash
mvn javafx:run
```

## Project Structure

- `src/main/java/` - Java source code
  - `Controllers/` - MVC controllers
  - `Models/` - Data models
  - `Views/` - View factory and UI components
- `src/main/resources/` - Resource files
  - `Fxml/` - JavaFX FXML layout files
  - `Styles/` - CSS style files
  - `db/` - Database related files

## Key Features

- User Authentication (Login/Register)
- Account Management (Create/View accounts)
- Transaction Processing (Deposit/Withdrawal/Transfer)
- Transaction History
- User Profile Management

## User Roles

The system supports three user roles:

1. Client
- View account balance
- Make transfers
- View transaction history

2. Employee
- Create customer accounts
- Process deposits

3. Administrator
- Manage user accounts
- System configuration management

## Technology Stack

- JavaFX - UI Framework
- SQLite - Database
- Maven - Project Management
- CSS - Styling

### Prerequisites
- JDK 21
- Maven
- IDE (IntelliJ IDEA recommended)

### Setup Development Environment
1. Open project in IDE
2. Import as Maven project
3. Run `mvn clean install` to resolve dependencies
4. Run `BankApplication.java` to start the application

## Developer

- Author: Devin Studd, Fanghao Meng