package com.karol.app.service;

import com.karol.app.model.Booking;
import com.karol.app.model.Flight;
import com.karol.app.model.Role;
import com.karol.app.model.User;
import com.karol.app.repository.BookingRepository;
import com.karol.app.repository.FlightRepository;
import com.karol.app.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;

    private UserRepository userRepository;

    private FlightRepository flightRepository;

    public BookingServiceImpl (BookingRepository bookingRepository, UserRepository userRepository,
                               FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Page<Booking> getAllBookingsSortedBy(String field, int page, int records) {
        if (!Arrays.asList("id", "bookingDate", "bookedSeatsNumber").contains(field))
            return null;
        return bookingRepository.findAll(PageRequest.of(page, records,
                Sort.by(Sort.Direction.ASC, field)));
    }

    @Override
    public Booking getBookingById(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.isPresent() ? booking.get() : null;
    }


    @Override
    public Booking createBooking (Booking booking, String username) {
        Optional<User> passenger = userRepository.findByEmail(username);
        Optional<Flight> flight = flightRepository.findById(booking.getFlight().getId());

        if (!passenger.isPresent() || !flight.isPresent()
                || flight.get().getDepartureDate().isBefore(LocalDateTime.now()))
            return null;

        booking.setPassenger(passenger.get());
        booking.setFlight(flight.get());
        booking.setBookingDate(LocalDateTime.now());

        int numberOfBookedSeats = 0;
        Collection<Booking> bookings = flight.get().getBookedSeats();
        if (bookings.size() > 0)
            for (Booking b : bookings)
                numberOfBookedSeats += b.getBookedSeatsNumber();

        if (numberOfBookedSeats + booking.getBookedSeatsNumber() > flight.get().getMaxPassengersNumber())
            return null;
        booking.setId(null);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking editBookingById(long id, Booking booking) {
        Optional<Booking> bookingFromDb = bookingRepository.findById(id);
        if (bookingFromDb.isPresent()) {

            int numberOfBookedSeats = 0;
            Collection<Booking> bookings = bookingFromDb.get().getFlight().getBookedSeats();
            if (bookings.size() > 0)
                for (Booking b : bookings)
                    numberOfBookedSeats += b.getBookedSeatsNumber();
            numberOfBookedSeats -= bookingFromDb.get().getBookedSeatsNumber();

            if (bookingFromDb.get().getFlight().getMaxPassengersNumber() < numberOfBookedSeats + booking.getBookedSeatsNumber())
                return null;
            bookingFromDb.get().setBookedSeatsNumber(booking.getBookedSeatsNumber());
            return bookingRepository.save(bookingFromDb.get());
        }
        return null;
    }

    @Override
    public boolean removeBookingById(long id) {
        Optional<Booking> bookingFromDb = bookingRepository.findById(id);
        if (bookingFromDb.isPresent()) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isAbleToChange(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.isPresent() && booking.get().getFlight().getDepartureDate().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean hasAccess(String username, long itemId) {
        Optional<User> user = userRepository.findByEmail(username);
        Optional<Booking> booking = bookingRepository.findById(itemId);
        return user.isPresent() && booking.isPresent() &&
                (booking.get().getPassenger().getId().equals(user.get().getId()) || user.get().getRole() == Role.ADMIN);
    }

    @Override
    public boolean hasAccessToUserWithId(String username, long itemId) {
        Optional<User> user = userRepository.findByEmail(username);
        return user.isPresent() && (user.get().getId() == itemId || user.get().getRole() == Role.ADMIN);
    }

    @Override
    public Collection<Booking> getBookingsOfPassengerById(long userId) {
        return bookingRepository.findByPassengerId(userId);
    }

    @Override
    public Collection<Booking> getBookingsOfFlightById(long flightId) {
        return bookingRepository.findByFlightId(flightId);
    }

}
