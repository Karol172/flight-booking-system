package com.karol.app.controller;

import com.karol.app.dto.AirportDto;
import com.karol.app.dto.UserDto;
import com.karol.app.model.Airport;
import com.karol.app.model.User;
import com.karol.app.service.AirportService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/airport")
public class AirportController {

    private AirportService airportService;

    private ModelMapper modelMapper;

    public AirportController (AirportService airportService, ModelMapper modelMapper) {
        this.airportService = airportService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(airportService.getAllAirports().stream()
                        .map(airport -> modelMapper.map(airport, AirportDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity one (@PathVariable("id") long id) {
        Optional<Airport> airport = airportService.getAirportById(id);
        return airport.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(airport.get(),
                AirportDto.class)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity create (@RequestBody @Valid AirportDto airportDto) {
        return airportService.createAirport(modelMapper.map(airportDto, Airport.class)).isPresent() ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid AirportDto airportDto) {
        return airportService.editAirportById(id, modelMapper.map(airportDto, Airport.class)) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove (@PathVariable("id") long id) {
        return airportService.removeAirportById(id) ?
                ResponseEntity.status(HttpStatus.OK).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
