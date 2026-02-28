# KBC GUI - Kaun Banega Crorepati (Attack on Titan Edition)

A Swing-based GUI version of the KBC quiz game.

## Requirements
- Java JDK 8 or higher

## Project Structure
```
KBC_GUI/
├── src/
│   └── main/
│       ├── Question.java        # Question model
│       ├── QuestionBank.java    # All 16 questions
│       └── KBCMain.java         # Main GUI application
├── run.bat                      # Windows build & run
├── run.sh                       # Linux/Mac build & run
└── README.md
```

## How to Run

### Windows
Double-click `run.bat` or run in Command Prompt:
```
run.bat
```

### Linux / Mac
```bash
chmod +x run.sh
./run.sh
```

### Manual (any OS)
```bash
mkdir out
javac -d out src/main/Question.java src/main/QuestionBank.java src/main/KBCMain.java
java -cp out main.KBCMain
```

## Features
- 🎨 Beautiful dark-blue KBC-themed GUI
- 📋 16 Attack on Titan questions with prize amounts
- 💡 **Audience Poll** lifeline – shows percentage distribution
- 🔢 **50:50 lifeline** – removes two wrong answers
- 🏆 Prize ladder displayed on the right (highlights current question)
- 🚪 Quit button to leave with your current winnings
- ✅ Color-coded answer reveal (green = correct, red = wrong)
- 🔒 Safe zones at ₹5,000 (Q4) and ₹80,000 (Q8)
- 🔄 Play again without restarting the app
