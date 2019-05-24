package com.karol.app.controller;

import com.karol.app.dto.BookingDto;
import com.karol.app.model.Booking;
import com.karol.app.service.BookingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation("Get all bookings")
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings().stream()
                .peek(booking -> booking.getPassenger().setPassword(null))
                .map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Get all bookings, sort and divide into certain number of pages")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") @ApiParam("Name of the sorting field") String field,
                                           @PathVariable("page") @ApiParam("Page number") int page,
                                           @PathVariable("records") @ApiParam("Number of records on a single page") int records) {
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
    @ApiOperation("Get bookings belongs to passenger")
    public ResponseEntity bookingsFilteredByPassenger(@PathVariable("passengerId") @ApiParam("Id of passenger") long passengerId,
                                                      Principal principal) {
        if (!bookingService.hasAccessToUserWithId(principal.getName(), passengerId))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingsOfPassengerById(passengerId)
                .stream().peek(flight -> flight.getPassenger().setPassword(null))
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/flight/{flightId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Get bookings to flight")
    public ResponseEntity bookingsFilteredByFlight (@PathVariable("flightId") @ApiParam("Id of flight") long flightId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingsOfFlightById(flightId)
                .stream().peek(flight -> flight.getPassenger().setPassword(null))
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Get booking")
    public ResponseEntity one (@PathVariable("id") @ApiParam("Id of booking") long id, Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(booking, BookingDto.class));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Create new booking")
    public ResponseEntity create (@RequestBody @Valid @ApiParam("Booking") BookingDto bookingDto, Principal principal) {
        Booking booking = bookingService.createBooking(modelMapper.map(bookingDto, Booking.class), principal.getName());
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(booking, BookingDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Update booking")
    public ResponseEntity update (@PathVariable("id") @ApiParam("Id of booking") long id,
                                  @RequestBody @Valid @ApiParam("Booking") BookingDto bookingDto,
                                  Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if (!bookingService.isAbleToChange(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Booking booking = bookingService.editBookingById(id, modelMapper.map(bookingDto, Booking.class));
        if (booking == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        booking.getPassenger().setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(booking, BookingDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Delete booking")
    public ResponseEntity remove (@PathVariable("id") @ApiParam("Id of booking") long id, Principal principal) {
        if (!bookingService.hasAccess(principal.getName(), id))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if (!bookingService.isAbleToChange(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (!bookingService.removeBookingById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
