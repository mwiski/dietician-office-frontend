#### **Dietician office application**
1. #### **General description**
	This is a java frontend project for an application to schedule visits to dieticians, created using Vaadin library
2. #### **Link to last commit**
	https://github.com/mwiski/dietician-office-frontend/commit/91cdb9f10682ff61445590b1bd8f32cbe31c6244
3. #### **Requirements before running**
    - The application has been created using Java 8, Gradle, MySql and H2 databases, Spring and Hibernate frameworks, runs on the embedded Tomcat server. The detailed information is included in build.gradle.
    - Before running the application make sure you have the appropriate software installed. Credentials for the databases are listed in application.properties file.
    - Remember to add api.key that you set in backend project. 
4. #### **Running the application**
    - Application is set to run on port 8080
	- Before running the application build it using 'gradlew build' command in the terminal of your IDE. To start the application run a file called DieticianFrontendApplication situated in the package 'pl.mwiski.dieticianfrontend'.
	- Urls in feign clients are set to http://localhost:8081 so you can run application on your local server.
	- Remember to run DieticianOffice (backend) application before frontend
	- You may want to first add admin to your application with backend of Dietician Office app, for example using Postman
5. #### **Backend**
    Link to github project with backend to Dietician office application
    https://github.com/mwiski/dietician-office
6. #### **Additional comment**
    This project was created only to show how Dietician Office app may run. Work is in progress. May contain bugs :)
