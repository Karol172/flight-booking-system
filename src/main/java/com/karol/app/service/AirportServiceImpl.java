package com.karol.app.service;

import com.karol.app.model.Airport;
import com.karol.app.repository.AirportRepository;
import org.springframework.stereotype.Service;

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

    @Override
    public Airport getAirportById(long id) {
        Optional<Airport> airport = airportRepository.findById(id);
        return airport.isPresent() ? airport.get() : null;
    }

    @Override
    public Airport createAirport(Airport airport) {
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
