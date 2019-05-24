package com.karol.app.service;

import com.karol.app.model.Booking;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface BookingService {

    Collection<Booking> getAllBookings();

    Page<Booking> getAllBookingsSortedBy(String field, int page, int records);

    Booking getBookingById(long id);

    Booking createBooking(Booking booking, String username);

    Booking editBookingById(long id, Booking booking);

    boolean removeBookingById(long id);

    boolean isAbleToChange(long id);

    Collection<Booking> getBookingsOfPassengerById(long userId);

    Collection<Booking> getBookingsOfFlightById(long flightId);

    boolean hasAccess(String username, long itemId);

    boolean hasAccessToUserWithId(String username, long itemId);

}
