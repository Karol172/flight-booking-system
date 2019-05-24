package com.karol.app.controller;

import com.karol.app.dto.AirportDto;
import com.karol.app.model.Airport;
import com.karol.app.service.AirportService;
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
@RequestMapping("/api/airport")
public class AirportController {

    private AirportService airportService;

    private ModelMapper modelMapper;

    public AirportController (AirportService airportService, ModelMapper modelMapper) {
        this.airportService = airportService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity all () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(airportService.getAllAirports().stream()
                        .map(airport -> modelMapper.map(airport, AirportDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/sort/{sortedBy}/page/{page}/{records}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity sortAndPaginate (@PathVariable("sortedBy") String field, @PathVariable("page") int page,
                                           @PathVariable("records") int records) {
        if (page < 0 || records < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Page<Airport> airportPage= airportService.getAllAirportsSortedBy(field, page, records);

        if (airportPage == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK)
                .body(airportPage.stream()
                        .map(airport -> modelMapper.map(airport, AirportDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity one (@PathVariable("id") long id) {
        Airport airport = airportService.getAirportById(id);
        if (airport == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(airport, AirportDto.class));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity create (@RequestBody @Valid AirportDto airportDto) {
        Airport responseAirport = airportService.createAirport(modelMapper.map(airportDto, Airport.class));
        if (responseAirport.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(responseAirport, AirportDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity update (@PathVariable("id") long id, @RequestBody @Valid AirportDto airportDto) {
        Airport responseAirport = airportService.editAirportById(id, modelMapper.map(airportDto, Airport.class));
        if (responseAirport == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(responseAirport, AirportDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity remove (@PathVariable("id") long id) {
        if (!airportService.removeAirportById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
