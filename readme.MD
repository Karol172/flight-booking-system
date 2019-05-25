# Flight Booking System

## Description
This application is simple system of flights booking.

It can be used by just registered users. In app are two group of users: ADMIN and USER. Admin has access to all data. User has access only for his own bookings and also can review flights.

## Running instructions
1. Clone repository or download and extract ZIP
2. `cd` into main directory of repository
3. Run `./mvnw spring-boot:run` if running on Unix platform, or `mvnw.cmd spring-boot:run` for Windows OS
4. Now app is working at port 8080

You can use existing users. In database are three users:

1. USERNAME: joe.doe@app.com PASSWORD: joepassword ROLE: ADMIN  
2. USERNAME: marry.kowalski@app.com PASSWORD: marrypassword ROLE: USER   
3. USERNAME: j.nowak@app.com PASSWORD: johnpassword ROLE: USER  

## Documentation

Go to http://localhost:8080/swagger-ui.html if you want to see REST Api docs.