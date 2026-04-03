# 🎓 Student Management System
### Java Swing + JDBC + JavaBeans + MVC Architecture

> **VIT University — Java Programming Course Project**  
> Roll: 24BCE10360

---

## 📚 Syllabus Coverage

| Unit | Topic Covered | Where in Code |
|------|--------------|---------------|
| **Unit 2** | JavaBeans (default constructor, getters/setters, Serializable) | `model/Student.java` |
| **Unit 2** | Custom Event Types (`StudentEvent`) | `events/StudentEvent.java` |
| **Unit 2** | Custom EventListener (`StudentEventListener`) | `events/StudentEventListener.java` |
| **Unit 2** | JavaBeans Event Model (`addStudentEventListener`) | `controller/StudentController.java` |
| **Unit 3** | JDBC Connection + Driver Loading | `dao/DatabaseConnection.java` |
| **Unit 3** | PreparedStatement, ResultSet, SQL-to-Java mapping | `dao/StudentDAO.java` |
| **Unit 3** | Coding Transactions (commit/rollback) | `dao/StudentDAO.java` |
| **Unit 4** | Swing MVC Design Pattern | All `view/` classes |
| **Unit 4** | JTable + Custom TableModel | `view/StudentTableModel.java` |
| **Unit 4** | JMenuBar + Mnemonics + Accelerators | `view/MainFrame.java` |
| **Unit 4** | JTabbedPane, JDialog, JFileChooser | `view/MainFrame.java` |
| **Unit 4** | GridBagLayout, BorderLayout, FlowLayout | `view/StudentFormDialog.java` |
| **Unit 4** | Text Fields, Combo Boxes, Radio Buttons, Borders | `view/StudentFormDialog.java` |
| **Unit 4** | Tooltips, Option Dialogs, Confirmation Dialogs | `view/MainFrame.java` |

---

## 🗂️ Project Structure

```
StudentManagementSystem/
├── src/
│   ├── Main.java                          ← Entry point
│   ├── model/
│   │   └── Student.java                  ← JavaBean (Unit 2)
│   ├── events/
│   │   ├── StudentEvent.java             ← Custom Event (Unit 2)
│   │   └── StudentEventListener.java     ← Custom Listener (Unit 2)
│   ├── dao/
│   │   ├── DatabaseConnection.java       ← JDBC Connection (Unit 3)
│   │   └── StudentDAO.java               ← CRUD + Transactions (Unit 3)
│   ├── controller/
│   │   └── StudentController.java        ← MVC Controller (Unit 4)
│   └── view/
│       ├── MainFrame.java                ← Main Swing Window (Unit 4)
│       ├── StudentFormDialog.java        ← Add/Edit Dialog (Unit 4)
│       └── StudentTableModel.java        ← Custom TableModel (Unit 4)
├── database/
│   └── schema.sql                        ← Reference schema
├── pom.xml                               ← Maven build file
└── README.md
```

---

## ⚙️ Prerequisites

- **Java JDK 17+** (recommended: JDK 17 or 21)
- **Maven 3.6+** OR download `sqlite-jdbc-3.45.1.0.jar` manually

---

## 🚀 How to Run

### Option A — Maven (Recommended)

```bash
# 1. Go to project folder
cd StudentManagementSystem

# 2. Build the fat JAR (downloads sqlite-jdbc automatically)
mvn clean package -q

# 3. Run the application
java -jar target/StudentManagementSystem-1.0-jar-with-dependencies.jar
```

### Option B — Manual Compile (No Maven)

```bash
# 1. Download sqlite-jdbc JAR from:
#    https://github.com/xerial/sqlite-jdbc/releases
#    Place it inside the /lib folder as: lib/sqlite-jdbc.jar

# 2. Compile all Java files
javac -cp "lib/sqlite-jdbc.jar" -d out \
  src/model/Student.java \
  src/events/StudentEvent.java \
  src/events/StudentEventListener.java \
  src/dao/DatabaseConnection.java \
  src/dao/StudentDAO.java \
  src/controller/StudentController.java \
  src/view/StudentTableModel.java \
  src/view/StudentFormDialog.java \
  src/view/MainFrame.java \
  src/Main.java

# 3. Run
java -cp "out:lib/sqlite-jdbc.jar" Main
# On Windows: java -cp "out;lib/sqlite-jdbc.jar" Main
```

### Option C — IntelliJ IDEA (Easiest)

1. Open IntelliJ → **File → Open** → select `StudentManagementSystem/` folder
2. IntelliJ detects `pom.xml` → click **"Load Maven Project"**
3. Right-click `Main.java` → **Run 'Main.main()'**

---

## 🖥️ Features

| Feature | Description |
|---------|-------------|
| ➕ **Add Student** | Form dialog with validation |
| ✏️ **Edit Student** | Double-click row or click Edit button |
| 🗑️ **Delete Student** | With confirmation dialog |
| 🔍 **Search** | Live search by name, roll, course, branch |
| 📊 **Statistics** | Total students, average CGPA |
| 📤 **Export CSV** | Export all records to CSV file |
| ⌨️ **Keyboard Shortcuts** | Ctrl+N (Add), F2 (Edit), Del (Delete), F5 (Refresh) |

---

## 🗄️ Database

- Uses **SQLite** — no separate server setup needed
- Database file `students.db` auto-created in the run directory
- 5 sample student records seeded on first launch

---

## 📐 Architecture Diagram

```
┌─────────────────────────────────────────────────────┐
│                    VIEW (Swing)                      │
│  MainFrame ──→ StudentFormDialog                    │
│  StudentTableModel (Custom JTable model)            │
└──────────────────────┬──────────────────────────────┘
                       │ calls / listens (events)
┌──────────────────────▼──────────────────────────────┐
│                 CONTROLLER (MVC)                     │
│  StudentController                                  │
│  - fires StudentEvent (JavaBean event model)        │
│  - validates input                                  │
└──────────────────────┬──────────────────────────────┘
                       │ calls
┌──────────────────────▼──────────────────────────────┐
│                  MODEL (Data)                        │
│  Student.java (JavaBean)                            │
│  StudentDAO.java (JDBC CRUD + Transactions)         │
│  DatabaseConnection.java (SQLite via JDBC)          │
└─────────────────────────────────────────────────────┘
```

---

## 👨‍💻 Author

**Priyanshu Vashisth** | Roll: 24BCE10360 | VIT University
