package com.karol.app.controller;

import com.karol.app.dto.AirportDto;
import com.karol.app.dto.FlightDto;
import com.karol.app.model.Airport;
import com.karol.app.model.Flight;
import com.karol.app.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") String field, @PathVariable("page") int page,
                                           @PathVariable("records") int records) {
        if (page < 0 || records < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Page<Flight> flightPage= flightService.getAllFlightsSortedBy(field, page, records);

        if (flightPage == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK)
                .body(flightPage.stream()
                        .map(airport -> modelMapper.map(airport, FlightDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/start/{airportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity flightsFilteredByStartingAirport(@PathVariable("airportId") long airportId) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getStartedFlightsFromAirportById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/destination/{airportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity flightsFilteredByDestinationAirport(@PathVariable("airportId") long airportId) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getFinishedFlightsFromAirportById(airportId).stream()
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

}
