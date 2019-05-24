package com.karol.app.service;

import com.karol.app.model.Airport;

import java.util.Collection;

public interface AirportService {

    Collection<Airport> getAllAirports ();

    Airport getAirportById (long id);

    Airport createAirport (Airport airport);

    Airport  editAirportById (Long id, Airport airport);

    boolean removeAirportById(long id);
}
