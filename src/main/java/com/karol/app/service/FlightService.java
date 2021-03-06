package com.karol.app.service;

import com.karol.app.model.Flight;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface FlightService {

    Collection<Flight> getAllFlights ();

    Page<Flight> getAllFlightsSortedBy (String field, int page, int records);

    Flight getFlightById (long id);

    Flight createFlight (Flight flight);

    Flight editFlightById (Long id, Flight flight);

    boolean removeFlightById(long id);

    Collection<Flight> getStartedFlightsFromAirportById (long airportId);

    Collection<Flight> getFinishedFlightsFromAirportById (long airportId);

}
