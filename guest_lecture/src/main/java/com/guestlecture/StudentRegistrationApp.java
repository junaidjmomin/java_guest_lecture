package com.guestlecture;

// JavaFX: classes used to build the desktop interface
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// JDBC: classes used to communicate with the MySQL database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// A JavaFX application extends Application instead of being a console-only program.
public class StudentRegistrationApp extends Application {

    // JavaFX calls start() after launch(). The Stage is the application window.
    @Override
    public void start(Stage stage) {

        // STEP 1: Create the controls that the user sees.
        // Label displays text; TextField accepts typed input; Button is clickable.
        Label heading = new Label("Student Registration");

        TextField name = new TextField();
        name.setPromptText("Enter name"); // Hint shown while the field is empty

        TextField email = new TextField();
        email.setPromptText("Enter email");

        TextField marks = new TextField();
        marks.setPromptText("Enter marks");

        Button save = new Button("Save Student");
        Label status = new Label("Fill the fields and click Save.");

        // STEP 2: Handle the Save button click.
        // setOnAction() registers code to run when the user clicks this button.
        save.setOnAction(event -> {

            // getText() reads a TextField as a String.
            // trim() removes extra spaces before and after the user's input.
            String studentName = name.getText().trim();
            String studentEmail = email.getText().trim();
            String studentMarks = marks.getText().trim();

            // Stop here if any required field is blank.
            if (studentName.isEmpty() || studentEmail.isEmpty()
                    || studentMarks.isEmpty()) {
                status.setText("Please fill all the fields.");
                return; // Exit this button-click handler, not the whole application
            }

            // STEP 3: Convert and validate the marks.
            // TextField gives us text, but the database column stores an integer.
            int score;
            try {
                score = Integer.parseInt(studentMarks); // e.g. "85" becomes 85
            } catch (NumberFormatException ex) {
                // This runs when the text is not an integer, such as "abc".
                status.setText("Marks must be a whole number.");
                return;
            }

            if (score < 0 || score > 100) {
                status.setText("Marks must be between 0 and 100.");
                return;
            }

            // STEP 4: Prepare the database connection and SQL command.
            // localhost = this computer; 3306 = MySQL's usual port;
            // studentdb = database created by sql/setup.sql.
            String url = "jdbc:mysql://localhost:3306/studentdb";

            // Read credentials from environment variables, not from the source file.
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            // INSERT adds a new row to the students table.
            // Each ? is a placeholder for one value we will supply safely below.
            String sql = "INSERT INTO students (name, email, marks) VALUES (?, ?, ?)";

            // STEP 5: Connect to MySQL and send the INSERT command.
            // DriverManager opens the JDBC Connection.
            // PreparedStatement stores the SQL command and its parameters.
            // try-with-resources automatically closes both objects afterward.
            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                // Replace the ? placeholders in order. JDBC parameters start at 1.
                ps.setString(1, studentName);   // First ?: name
                ps.setString(2, studentEmail);  // Second ?: email
                ps.setInt(3, score);            // Third ?: marks (integer)

                // executeUpdate() runs INSERT/UPDATE/DELETE statements.
                // It returns the number of rows affected; here we don't use it.
                ps.executeUpdate();

                // STEP 6: Give feedback and clear inputs after a successful save.
                status.setText("Student saved successfully!");
                name.clear();
                email.clear();
                marks.clear();
            } catch (SQLException ex) {
                // Show an error if MySQL cannot connect or the query fails.
                status.setText("Database error: " + ex.getMessage());
            }
        });

        // STEP 7: Arrange the controls vertically.
        // VBox(10, ...) uses 10 pixels of spacing between child elements.
        VBox root = new VBox(10,
                heading,
                new Label("Name:"), name,
                new Label("Email:"), email,
                new Label("Marks:"), marks,
                save, status);
        root.setPadding(new Insets(20)); // 20 pixels inside the outer edge

        // STEP 8: Put the layout inside a Scene, then show the Stage.
        // Stage = window; Scene = content area; VBox = layout holding controls.
        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("JavaFX + JDBC Hands-on");
        stage.show(); // Without show(), the window would not appear
    }

    // The main() method starts the JavaFX application lifecycle.
    public static void main(String[] args) {
        launch(args);
    }
}
