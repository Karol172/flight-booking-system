package com.karol.app.service;

import com.karol.app.model.Booking;
import com.karol.app.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;

    public BookingServiceImpl (BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Collection<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(long id) {
        return bookingRepository.findById(id);
    }

    // TODO: Max number of booked seats
    @Override
    public Optional<Booking> createBooking(Booking booking) {
        return Optional.of(bookingRepository.save(booking));
    }

    @Override
    public boolean editBookingById(Long id, Booking booking) {
        Optional<Booking> bookingFromDb = bookingRepository.findById(id);
        if (bookingFromDb.isPresent()) {
            bookingFromDb.get().setBookedSeatsNumber(booking.getBookedSeatsNumber());
            bookingFromDb.get().setBookingDate(booking.getBookingDate());
            bookingFromDb.get().setFlight(booking.getFlight());
            bookingFromDb.get().setPassenger(booking.getPassenger());
            bookingRepository.save(bookingFromDb.get());
            return true;
        }
        return false;
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
    public Collection<Booking> getUserBookingById(long userId) {
        return bookingRepository.findByPassengerId(userId);
    }

    @Override
    public Collection<Booking> getFlightBookingById(long flightId) {
        return bookingRepository.findByFlightId(flightId);
    }
}
