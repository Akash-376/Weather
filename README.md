# Weather App
<br>

![weather_home](https://github.com/Akash-376/Weather/assets/112763866/f18d91f5-7aba-4fbb-bb34-6ca0e31fe7f5)

## Weather details for New Delhi
![delhi_temp](https://github.com/Akash-376/Weather/assets/112763866/0543ab39-3d6f-4ce6-818e-436955d961d5)

# Description
The Weather App is a Spring Boot application designed to provide users with real-time weather information for different cities. With a user-friendly interface, the app allows users to retrieve and store weather data effortlessly.


## Prerequisites
Before you begin, ensure you have met the following requirements:

- Java: You need Java 8 or higher installed on your system.
- MySQL: Make sure you have a MySQL database instance set up.
- Maven: You'll need Maven as the build tool.

## Frameworks_and_Libraries_Used
- Spring Boot: A powerful framework for building Java applications.
- Hibernate: Object-relational mapping library.
- Lombok: For reducing boilerplate code.

## Installation
1. Clone the repository:
```
https://github.com/Akash-376/Weather.git
```
2. Navigate to the project directory:
   In which POM.xml is visible
```
cd WeatherApp
```
3. Build the project using Maven:
```
mvn clean install
```

## Configuration
1. Create the database named 'weather' using MySQL Workbench
2. Open application.properties in the src/main/resources directory.
3. Configure your database settings:
```
spring.datasource.url=jdbc:mysql://localhost:3306/weather
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```
Replace username and password with yours.

## Running the Application
1. Run the application using Maven:
```
mvn spring-boot:run
```
2. The application should now be running on the specified port (default is 8080).




