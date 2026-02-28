# Global Quest Technologies — Course Management System

A complete Java Swing GUI application for course management.

## Project Structure
```
GQT/
├── src/main/
│   ├── Course.java              # Course model
│   ├── Student.java             # Student model (register/pay courses)
│   ├── Professor.java           # Professor model
│   ├── CourseManager.java       # Central in-memory data store
│   ├── Theme.java               # Shared UI theming utilities
│   ├── MainWindow.java          # Landing page with 3 role buttons
│   ├── StudentLoginWindow.java  # Student login screen
│   ├── StudentDashboard.java    # Student dashboard (4 modules)
│   ├── ProfessorWindow.java     # Professor registration + course browser
│   └── AdminWindow.java         # Admin panel (courses + students)
├── run.bat                      # Windows build & run
├── run.sh                       # Linux/Mac build & run
└── README.md
```

## How to Run

### Windows
```
run.bat
```

### Linux / Mac
```bash
chmod +x run.sh && ./run.sh
```

### Manual
```bash
mkdir out
javac -d out src/main/Course.java src/main/Student.java src/main/Professor.java src/main/CourseManager.java src/main/Theme.java src/main/StudentLoginWindow.java src/main/StudentDashboard.java src/main/ProfessorWindow.java src/main/AdminWindow.java src/main/MainWindow.java
java -cp out main.MainWindow
```

## Default Credentials

### Students (demo accounts)
| Username | Password  | Name          |
|----------|-----------|---------------|
| alice    | alice123  | Alice Johnson |
| bob      | bob123    | Bob Smith     |
| carol    | carol123  | Carol White   |

### Admin
Password: `admin123`

### Professor
No login required — fill out the registration form to create a new professor account.

## Features

### Student Module
- Login with username/password validation
- View all available courses in a table
- Apply for courses (with confirmation dialog)
- View registered courses with paid/pending status
- Pay for individual courses (simulated payment dialog)

### Professor Module
- Registration form (Name, Subject, Email, Experience)
- View registered professors in a JTable
- Browse all available courses
- Apply to teach a selected course

### Admin Module
- Password-protected login
- Add new courses (name + fee)
- Remove existing courses (with confirmation)
- View all students with enrollment/payment stats
