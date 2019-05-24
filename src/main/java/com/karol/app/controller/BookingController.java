package com.karol.app.controller;

import com.karol.app.dto.BookingDto;
import com.karol.app.model.Booking;
import com.karol.app.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private BookingService bookingService;

    private ModelMapper modelMapper;

    public BookingController (BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings().stream()
                .peek(booking -> booking.getPassenger().setPassword(null))
                .map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") String field, @PathVariable("page") int page,
                                           @PathVariable("records") int records) {
        if (page < 0 || records < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Page<Booking> bookingPage = bookingService.getAllBookingsSortedBy(field, page, records);

        if (bookingPage == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingPage.stream().peek(booking -> booking.getPassenger().setPassword(null))
                        .map(airport -> modelMapper.map(airport, BookingDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/passenger/{passengerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity bookingsFilteredByPassenger(@PathVariable("passengerId") long passengerId, Principal principal) {
        if (!bookingService.hasAccessToUserWithId(principal.getName(), passengerId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingsOfPassengerById(passengerId)
                .stream().peek(flight -> flight.getPassenger().setPassword(null))
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/flight/{flightId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity bookingsFilteredByFlight (@PathVariable("flightId") long flightId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingsOfFlightById(flightId)
                .stream().peek(flight -> flight.getPassenger().setPassword(null))
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity one (@PathVariable("id") long id, Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(booking, BookingDto.class));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity create (@RequestBody @Valid BookingDto bookingDto, Principal principal) {
        Booking booking = bookingService.createBooking(modelMapper.map(bookingDto, Booking.class), principal.getName());
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(booking, BookingDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid BookingDto bookingDto, Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (!bookingService.isAbleToChange(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Booking booking = bookingService.editBookingById(id, modelMapper.map(bookingDto, Booking.class));
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(booking, BookingDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity remove (@PathVariable("id") long id, Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (!bookingService.isAbleToChange(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (!bookingService.removeBookingById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
