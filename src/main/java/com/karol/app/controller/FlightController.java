package com.karol.app.controller;

import com.karol.app.dto.FlightDto;
import com.karol.app.model.Flight;
import com.karol.app.service.FlightService;
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
    @ApiOperation("Get all flights")
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getAllFlights().stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Get all flights, sort and divide into certain number of pages")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") @ApiParam("Name of the sorting field") String field,
                                           @PathVariable("page") @ApiParam("Page number") int page,
                                           @PathVariable("records") @ApiParam("Number of records on a single page") int records) {
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
    @ApiOperation("Get flights with starting airport")
    public ResponseEntity flightsFilteredByStartingAirport(@PathVariable("airportId") @ApiParam("Id of airport") long airportId) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getStartedFlightsFromAirportById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/destination/{airportId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Get flights with destination airport")
    public ResponseEntity flightsFilteredByDestinationAirport(@PathVariable("airportId") @ApiParam("Id of airport") long airportId) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getFinishedFlightsFromAirportById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ApiOperation("Get flight")
    public ResponseEntity one (@PathVariable("id") @ApiParam("Id of flight") long id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(flight, FlightDto.class));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Create new flight")
    public ResponseEntity create (@RequestBody @Valid @ApiParam("Flight") FlightDto flightDto) {
        Flight flight = flightService.createFlight(modelMapper.map(flightDto, Flight.class));
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(flight, FlightDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Update flight")
    public ResponseEntity update (@PathVariable("id") @ApiParam("Id of user") long id,
                                  @RequestBody @Valid @ApiParam("Flight") FlightDto flightDto) {
        Flight flight = flightService.editFlightById(id, modelMapper.map(flightDto, Flight.class));
        if (flight == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(flight, FlightDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("Delete flight")
    public ResponseEntity remove (@PathVariable("id") @ApiParam("Id of user") long id) {
        if (!flightService.removeFlightById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
