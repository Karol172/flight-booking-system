package com.karol.app.service;

import com.karol.app.model.Airport;
import com.karol.app.repository.AirportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AirportServiceImpl implements AirportService {

    private AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Collection<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    public Page<Airport> getAllAirportsSortedBy(String field, int page, int numberRecord) {
        if (!Arrays.asList("id", "city", "country").contains(field))
            return null;
        return airportRepository.findAll(PageRequest.of(page, numberRecord,
                Sort.by(Sort.Direction.ASC, field)));
    }

    @Override
    public Airport getAirportById(long id) {
        Optional<Airport> airport = airportRepository.findById(id);
        return airport.isPresent() ? airport.get() : null;
    }

    @Override
    public Airport createAirport(Airport airport) {
        airport.setId(null);
        return airportRepository.save(airport);
    }

    @Override
    public Airport editAirportById(Long id, Airport airport) {
        Optional<Airport> airportFromDb = airportRepository.findById(id);
        if (airportFromDb.isPresent()) {
            airportFromDb.get().setCity(airport.getCity());
            airportFromDb.get().setCountry(airport.getCountry());
            return airportRepository.save(airportFromDb.get());
        }
        return null;
    }

    @Override
    public boolean removeAirportById(long id) {
        Optional<Airport> airportFromDb = airportRepository.findById(id);
        if (airportFromDb.isPresent()) {
            airportRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
