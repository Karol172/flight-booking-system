package com.karol.app.service;

import com.karol.app.model.Airport;

import java.util.Collection;
import java.util.Optional;

public interface AirportService {

    Collection<Airport> getAllAirports ();

    Optional<Airport> getAirportById (long id);

    Optional<Airport> createAirport (Airport airport);

    boolean editAirportById (Long id, Airport airport);

    boolean removeAirportById(long id);
}
