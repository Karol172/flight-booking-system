package com.karol.app.controller;

import com.karol.app.dto.BookingDto;
import com.karol.app.model.Booking;
import com.karol.app.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
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
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings().stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity one (@PathVariable("id") long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(booking.get(), BookingDto.class)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid BookingDto bookingDto) {
        return bookingService.createBooking(modelMapper.map(bookingDto, Booking.class)).isPresent() ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.editBookingById(id, modelMapper.map(bookingDto, Booking.class)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove (@PathVariable("id") long id) {
        return bookingService.removeBookingById(id) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity passengerBooking(@PathVariable("passengerId") long passengerId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getUserBookingById(passengerId).stream()
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity flightBooking(@PathVariable("flightId") long flightId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getFlightBookingById(flightId).stream()
                .map(flight -> modelMapper.map(flight, BookingDto.class)).collect(Collectors.toList()));
    }
}
