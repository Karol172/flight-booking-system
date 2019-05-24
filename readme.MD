#Flight Booking System

##Description
This application is simple system of flight booking.

It can be used by just registered users. In app are two group of users: ADMIN and USER. Admin has access to all data. User has access only for his bookings and also can review available flights.

##Running instructions
1. Clone repository or download and extract ZIP
2. `cd` into main directory of repository
3. Run `./mvnw spring-boot:run` if running on Unix platform, or `./mvnw.cmd spring-boot:run` for Windows OS

In database are two users:
|USERNAME|PASSWORD|ROLE|
|||

You use application using basicauth.

##Documentation

Go to http://localhost:8080/swagger-ui.html if you want to see REST Api docs.