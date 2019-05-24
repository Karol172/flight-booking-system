package com.karol.app.service;

import com.karol.app.model.Airport;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface AirportService {

    Collection<Airport> getAllAirports ();

    Page<Airport> getAllAirportsSortedBy(String field, int page, int numberRecord);

    Airport getAirportById (long id);

    Airport createAirport (Airport airport);

    Airport  editAirportById (Long id, Airport airport);

    boolean removeAirportById(long id);

}
