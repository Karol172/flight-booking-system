package com.karol.app.controller;

import com.karol.app.dto.FlightDto;
import com.karol.app.model.Flight;
import com.karol.app.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
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
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getAllFlights().stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity one (@PathVariable("id") long id) {
        Optional<Flight> flight = flightService.getFlightById(id);
        return flight.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(flight.get(), FlightDto.class)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid FlightDto flightDto) {
        return flightService.createFlight(modelMapper.map(flightDto, Flight.class)).isPresent() ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid FlightDto flightDto) {
        return flightService.editFlightById(id, modelMapper.map(flightDto, Flight.class)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove (@PathVariable("id") long id) {
        return flightService.removeFlightById(id) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/destination/{airportId}")
    public ResponseEntity airportFlights(@PathVariable("airportId") long airportId) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.getAirportFlightsById(airportId).stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class)).collect(Collectors.toList()));
    }

}
