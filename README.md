# Service-Oriented Architecture Assignment #2

## Description
Java web application to store university students data to an XML file.

## Application Features
The program should allow users to perform the following actions:
* Add new students to the database with validations on all input fields.
* Update student details, all fields can be updated (except for ID).
* Search for a student with any of the fields and show the number of found students.
* Sort the data file using any of the student attributes based on the user input.
* Save the sorted file to disk after sorting by replacing the old unsorted file.

## XML File Template
> University.xml file should look something like this
```xml
<?xml version="1.0" encoding="UTF-8"?>
<University>
    <Student ID="20200134">
        <FirstName>Ahmed</FirstName>
        <LastName>Mohamed</LastName>
        <Gender>Male</Gender>
        <GPA>3.17</GPA>
        <Level>4</Level>
        <Address>Giza</Address>
    </Student>
</University>
```
