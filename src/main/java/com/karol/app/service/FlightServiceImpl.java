package com.karol.app.service;

import com.karol.app.model.Airport;
import com.karol.app.model.Flight;
import com.karol.app.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    public FlightServiceImpl (FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Collection<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Optional<Flight> getFlightById(long id) {
        return flightRepository.findById(id);
    }

    @Override
    public Optional<Flight> createFlight(Flight flight) {
        return Optional.of(flightRepository.save(flight));
    }

    @Override
    public boolean editFlightById(Long id, Flight flight) {
        Optional<Flight> flightFromDb = flightRepository.findById(id);
        if (flightFromDb.isPresent()) {
            flightFromDb.get().setAirplaneModel(flight.getAirplaneModel());
            flightFromDb.get().setDepartureDate(flight.getDepartureDate()); //TODO: Departure Date should be later than now
            flightFromDb.get().setDestinationAirport(flight.getDestinationAirport());
            flightFromDb.get().setMaxPassengersNumber(flight.getMaxPassengersNumber()); //TODO: Some limits
            flightRepository.save(flightFromDb.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFlightById(long id) {
        Optional<Flight> flightFromDb = flightRepository.findById(id);
        if (flightFromDb.isPresent()) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Collection<Flight> getAirportFlightsById(long airportId) {
        return flightRepository.findByDestinationAirportId(airportId);
    }
}
