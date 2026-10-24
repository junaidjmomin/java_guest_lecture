# JavaFX + JDBC — SIMPLE hands-on project

This is **only the Student Registration hands-on**, not the 13 introductory GUI demos.

It matches the corrected teaching slides in the following order:

1. **Slide 31** — Create `studentdb` and `students` in MySQL Workbench.
2. **Slide 32** — Create JavaFX `Label`, `TextField`, `Button`, and `VBox`.
3. **Slide 33** — Attach the Save button handler and execute JDBC `INSERT`.
4. **Slide 34** — Run `SELECT * FROM students;` in MySQL Workbench to verify the saved row.

The SQL shown separately on slides 27–28 is inside the Save button handler, not hidden in another Java class. There is **only one Java source file**. No `StudentRepository`, ListView, FXML, CSS, or extra application classes.

## Requirements

- JDK 21
- Maven 3.9+ (or Eclipse Maven integration)
- MySQL Server running at `localhost:3306`
- MySQL Workbench recommended for the SQL verification
- Network access the first time Maven downloads JavaFX and Connector/J

JavaFX and Connector/J are Maven dependencies in `pom.xml`. You do not manually copy JARs.

## Step A — Create the database

Connect in MySQL Workbench and run `sql/setup.sql` (click the lightning icon). It creates `studentdb.students` with `id`, `name`, `email`, `marks`.

## Step B — Set database credentials

Use the same MySQL credentials you use in Workbench. Do not put a real password in slides or source code.

**PowerShell (Windows)** — open a terminal in this project folder:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="YOUR_PASSWORD"
mvn clean javafx:run
```

**Command Prompt (Windows)**:

```cmd
set DB_USER=root
set DB_PASSWORD=YOUR_PASSWORD
mvn clean javafx:run
```

**macOS/Linux**:

```bash
export DB_USER=root
export DB_PASSWORD='YOUR_PASSWORD'
mvn clean javafx:run
```

**Eclipse:** File → Import → Maven → Existing Maven Projects → select the folder containing `pom.xml`. Right-click project → Run As → Maven build... → Goals: `clean javafx:run`. In the launch configuration's **Environment** tab add `DB_USER` and `DB_PASSWORD`.

## Step C — Test

1. Enter **Amit**, **amit@example.com**, and **85**.
2. Click **Save Student** and check the success message.
3. In MySQL Workbench run:

```sql
USE studentdb;
SELECT * FROM students;
```

A new row should appear. The email column is UNIQUE, so use a different email when testing again.

## Why the code is shorter than the old project

- **Old project:** JavaFX class + `StudentRepository` class + ListView, refresh button, and fuller validation.
- **This version:** one JavaFX class that runs `DriverManager.getConnection()`, `PreparedStatement`, and `executeUpdate()` directly when Save is clicked.

This is intentionally a *classroom example*, not production architecture. A larger program should separate database access from UI and run potentially slow database calls off the JavaFX UI thread. The demo has basic empty-input/marks validation, but does not fully validate email addresses.

## Common errors

- `Access denied` → check `DB_USER` and `DB_PASSWORD`.
- `Unknown database 'studentdb'` → execute `sql/setup.sql`.
- `No suitable driver` → allow Maven to download MySQL Connector/J; run `mvn clean javafx:run` from the project root.
- `Duplicate entry` → enter a different email (the SQL schema marks email as UNIQUE).
- JavaFX app fails to start → check `java -version` and `mvn -version`; run with JDK 21.
