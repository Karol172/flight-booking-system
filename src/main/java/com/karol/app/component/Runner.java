package com.karol.app.component;

import com.karol.app.model.*;
import com.karol.app.repository.AirportRepository;
import com.karol.app.repository.BookingRepository;
import com.karol.app.repository.FlightRepository;
import com.karol.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    private UserRepository userRepository;

    private AirportRepository airportRepository;

    private FlightRepository flightRepository;

    private BookingRepository bookingRepository;

    public Runner (UserRepository userRepository, AirportRepository airportRepository,
                   FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this. bookingRepository = bookingRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        createUsers();
        createAirports();
        createFlights();
        createBooking();
    }

    private void createUsers () {
        userRepository.save(new User("Joe", "Doe", Role.ADMIN, "joe.doe@app.com", "joespassword"));
        userRepository.save(new User("Mary", "Kowalski", Role.USER, "marry.kowalski@app.com", "marryspassword"));
    }

    private void createAirports () {
        airportRepository.save(new Airport("Warsaw", "Poland"));
        airportRepository.save(new Airport("Los Angeles", "United States"));
    }

    private void createFlights () {
        flightRepository.save(new Flight(LocalDateTime.now(), "Dreamliner 747", 370, airportRepository.findByCity("Warsaw")));
    }

    private void createBooking () {
        bookingRepository.save(new Booking(null, LocalDateTime.now(), 3, userRepository.findFirstByLastName("Doe"), flightRepository.getOne(1L)));
    }
}
