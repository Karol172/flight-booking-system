package com.karol.app.service;

import com.karol.app.model.Flight;

import java.util.Collection;
import java.util.Optional;

public interface FlightService {

    Collection<Flight> getAllFlights ();

    Optional<Flight> getFlightById (long id);

    Optional<Flight> createFlight (Flight flight);

    boolean editFlightById (Long id, Flight flight);

    boolean removeFlightById(long id);

    Collection<Flight> getAirportFlightsById (long airportId);
}
