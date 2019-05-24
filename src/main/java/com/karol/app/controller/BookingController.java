package com.karol.app.controller;

import com.karol.app.dto.BookingDto;
import com.karol.app.model.Booking;
import com.karol.app.service.BookingService;
import org.modelmapper.ModelMapper;
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

/*    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity passengerBooking(@PathVariable("passengerId") long passengerId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getUserBookingById(passengerId).stream()
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity flightBooking(@PathVariable("flightId") long flightId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getFlightBookingById(flightId).stream()
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }*/
}
