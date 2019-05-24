package com.karol.app.controller;

import com.karol.app.dto.FlightDto;
import com.karol.app.model.Flight;
import com.karol.app.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flight")
public class FlightController {

    private FlightService flightService;

    private ModelMapper modelMapper;

    public FlightController (FlightService flightService, ModelMapper modelMapper) {
        this.flightService = flightService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getAllFlights().stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity one (@PathVariable("id") long id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(flight, FlightDto.class));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity create (@RequestBody @Valid FlightDto flightDto) {
        Flight flight = flightService.createFlight(modelMapper.map(flightDto, Flight.class));
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(flight, FlightDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid FlightDto flightDto) {
        Flight flight = flightService.editFlightById(id, modelMapper.map(flightDto, Flight.class));
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(flight, FlightDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity remove (@PathVariable("id") long id) {
        if (!flightService.removeFlightById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

/*    @GetMapping("/destination/{airportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity airportFlights(@PathVariable("airportId") long airportId) {
        return null;*//*ResponseEntity.status(HttpStatus.OK).body(flightService.getAirportFlightsById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));*//*
    }

    @GetMapping("/destination/{airportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity airportFlights(@PathVariable("airportId") long airportId) {
        return null;*//*ResponseEntity.status(HttpStatus.OK).body(flightService.getAirportFlightsById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));*//*
    }*/

}
