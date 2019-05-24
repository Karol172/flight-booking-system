package com.karol.app.service;

import com.karol.app.model.Booking;

import java.util.Collection;

public interface BookingService {

    Collection<Booking> getAllBookings();

    Booking getBookingById(long id);

    Booking createBooking(Booking booking, String username);

    Booking editBookingById(long id, Booking booking);

    boolean removeBookingById(long id);

    boolean isAbleToChange(long id);

    Collection<Booking> getUserBookingById(long userId);

    Collection<Booking> getFlightBookingById(long flightId);

    boolean hasAccess(String username, long itemId);

}
