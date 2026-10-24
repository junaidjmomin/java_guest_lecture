-- Run this script in MySQL Workbench BEFORE running JavaFX.
CREATE DATABASE IF NOT EXISTS studentdb;
USE studentdb;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    marks INT NOT NULL
);

-- After clicking Save Student, run this query to see the new row:
SELECT * FROM students;
