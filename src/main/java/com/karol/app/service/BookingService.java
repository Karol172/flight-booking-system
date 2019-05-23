package com.karol.app.service;

import com.karol.app.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface BookingService {

    Collection<Booking> getAllBookings ();

    Optional<Booking> getBookingById (long id);

    Optional<Booking> createBooking (Booking booking);

    boolean editBookingById (Long id, Booking booking);

    boolean removeBookingById (long id);

    Collection<Booking> getUserBookingById (long userId);

    Collection<Booking> getFlightBookingById (long flightId);
}
