# Smart-Student-Routine-Expense-Manager
A Java-based desktop application to help students manage their daily routines, track expenses, and stay within budget. Built with Java Swing for the user interface and SQLite for local data storage.

## Features

- **Authentication:** Secure user registration and login.
- **Routine Management:** Create and manage daily schedules and tasks.
- **Expense Tracking:** Log daily expenses with categories.
- **Budget Management:** Set budgets and monitor your spending.
- **Reminders:** Set and manage reminders for important tasks.
- **Reports:** Generate and view detailed expense reports.

## Prerequisites

- Java Development Kit (JDK) installed and available in your system's PATH.
- Internet connection (only required for the first build to download the SQLite JDBC driver).

## How to Run

The project includes a convenient batch script for Windows to compile the source code, download dependencies, and start the application.

1. Open a command prompt or terminal in the project's root directory.
2. Run the start script:
   ```cmd
   run.bat
   ```
   *Note: On the first run, the script will automatically download the `sqlite-jdbc.jar` driver into the `lib` folder.*

## Project Structure

- `src/` - Contains all Java source code, structured by packages:
  - `model/` - Data models (User, Student, Routine, Expense, Budget, Reminder)
  - `dao/` - Data Access Objects for database interactions
  - `database/` - Database connection management
  - `service/` - Business logic and services
  - `view/` - Java Swing GUI components
  - `utils/` - Utility classes (e.g., password hashing, validation)
- `lib/` - Downloaded dependencies (SQLite JDBC driver)
- `out/` - Compiled Java `.class` files
- `run.bat` - Script to compile and execute the application

