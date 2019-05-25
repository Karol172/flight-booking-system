package com.karol.app.component;

import com.karol.app.model.*;
import com.karol.app.repository.AirportRepository;
import com.karol.app.repository.BookingRepository;
import com.karol.app.repository.FlightRepository;
import com.karol.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    private UserRepository userRepository;

    private AirportRepository airportRepository;

    private FlightRepository flightRepository;

    private BookingRepository bookingRepository;

    private PasswordEncoder passwordEncoder;

    public Runner (UserRepository userRepository, AirportRepository airportRepository,
                   FlightRepository flightRepository, BookingRepository bookingRepository,
                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createUsers();
        createAirports();
        createFlights();
        createBooking();
    }

    private void createUsers () {
        userRepository.save(new User("Joe", "Doe", Role.ADMIN, "joe.doe@app.com",
                passwordEncoder.encode("joepassword")));
        userRepository.save(new User("Mary", "Kowalski", Role.USER, "marry.kowalski@app.com",
                passwordEncoder.encode("marrypassword")));
        userRepository.save(new User("John", "Nowak", Role.USER, "j.nowak@app.com",
                passwordEncoder.encode("johnpassword")));
    }

    private void createAirports () {
        airportRepository.save(new Airport("Warsaw", "Poland"));
        airportRepository.save(new Airport("Paris", "France"));
        airportRepository.save(new Airport("Barcelona", "Spain"));
        airportRepository.save(new Airport("Berlin", "Germany"));
        airportRepository.save(new Airport("Rome", "Italy"));
        airportRepository.save(new Airport("New York", "United States"));
        airportRepository.save(new Airport("Dallas", "United States"));
        airportRepository.save(new Airport("San Antonio", "United States"));
        airportRepository.save(new Airport("Boston", "United States"));
        airportRepository.save(new Airport("Chicago", "United States"));
    }

    private void createFlights () {
        flightRepository.save(new Flight(LocalDateTime.now(), "Airbus A380 900", 900,
                airportRepository.findById(1L).get(), airportRepository.findById(2L).get()));
        flightRepository.save(new Flight(LocalDateTime.now().plusYears(1), "Boeing 747 8", 700,
                airportRepository.findById(3L).get(), airportRepository.findById(4L).get()));
        flightRepository.save(new Flight(LocalDateTime.now().plusDays(1), "Airbus A340 300", 295,
                airportRepository.findById(5L).get(), airportRepository.findById(6L).get()));
        flightRepository.save(new Flight(LocalDateTime.now().minusDays(5), "Boeing 777 200", 440,
                airportRepository.findById(7L).get(), airportRepository.findById(8L).get()));
        flightRepository.save(new Flight(LocalDateTime.now().plusMonths(1), "GULFSTREAM III", 12,
                airportRepository.findById(9L).get(), airportRepository.findById(10L).get()));
    }

    private void createBooking () {
        bookingRepository.save(new Booking(null, LocalDateTime.now(), 8,
                userRepository.findById(2L).get(), flightRepository.findById(5L).get()));
        bookingRepository.save(new Booking(null, LocalDateTime.now(), 3,
                userRepository.findById(3L).get(), flightRepository.findById(5L).get()));
        bookingRepository.save(new Booking(null, LocalDateTime.now(), 1,
                userRepository.findById(3L).get(), flightRepository.findById(1L).get()));
        bookingRepository.save(new Booking(null, LocalDateTime.now().minusDays(1), 1,
                userRepository.findById(3L).get(), flightRepository.findById(2L).get()));
        bookingRepository.save(new Booking(null, LocalDateTime.now(), 1,
                userRepository.findById(3L).get(), flightRepository.findById(3L).get()));
        bookingRepository.save(new Booking(null, LocalDateTime.now().minusWeeks(1), 1,
                userRepository.findById(3L).get(), flightRepository.findById(4L).get()));
    }

}
