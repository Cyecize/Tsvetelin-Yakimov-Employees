# TY - Employees

Simple Web application for demo purposes.  Written in **Java and JS**, the main goal is to process CSV files that contain logs of users and where they worked and determine which two employees worked together on projects for the longest time.

## Live Preview:
http://tsvetelin.yakimov.employees.cyecize.com

## Technologies used:
### Back End

- Java
- [Javache Web Server](https://github.com/Cyecize/Java-Web-Server) + Summer MVC Framework (My custom made Web server and MVC framework created in 2018)

### Front End
- Angular 14


# App is designed to follow these requirements:
Create an application that identifies the pair of employees who have worked together on common projects for the longest period of time. Input data:
A CSV file with data in the following format: **EmpID, ProjectID, DateFrom, DateTo**
Sample data:
143, 12, 2013-11-01, 2014-01-05
218, 10, 2012-05-16, NULL
143, 10, 2009-01-01, 2011-04-27
...
Sample output: 143, 218, 8
## Specific requirements
- DateTo can be NULL, equivalent to today
- The input data must be loaded to the program from a CSV file
- Create an UI: The user picks up a file from the file system and, after selecting it, all common projects of the pair are displayed in datagrid with the following columns:  Employee ID #1, Employee ID #2, Project ID, Days worked
- More than one date format to be supported, extra points will be given if all date formats are supported

## Edge cases
- A worker may have worked on a given project multiple times so this has to be accounted when doing the calculations

## Additional implementations
- ### Data validation
    - File validation (csv file type only and configurable max file size)
    - Data validation - After the content of the CSV is read, the data is validated for missing or invalid values
- ### Front end error handling
    - Validation errors are displayed on the front end, informing the user which row and field caused the error
- ### Dates
    - dd-MM-yyyy
    - yyyy-MM-dd
    - Designed to be easily upgraded to support much more.

## Build Docker image
- docker build -t ty-empl:1.0 .
- docker run -d --name ty-empls -p 7091:8000 ty-empl:1.0
