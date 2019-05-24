package com.karol.app.service;

import com.karol.app.model.Airport;
import com.karol.app.model.Booking;
import com.karol.app.model.Flight;
import com.karol.app.repository.AirportRepository;
import com.karol.app.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    private AirportRepository airportRepository;

    public FlightServiceImpl (FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public Collection<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Page<Flight> getAllFlightsSortedBy(String field, int page, int records) {
        if (!Arrays.asList("id", "departureDate", "airplaneModel", "maxPassengersNumber").contains(field))
            return null;
        return flightRepository.findAll(PageRequest.of(page, records,
                Sort.by(Sort.Direction.ASC, field)));
    }

    @Override
    public Flight getFlightById(long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.isPresent() ? flight.get() : null;
    }

    @Override
    public Flight createFlight(Flight flight) {
        if (!airportsExist(flight) || flight.getDepartureDate().isBefore(LocalDateTime.now()))
            return null;
        flight.setId(null);
        return flightRepository.save(flight);
    }

    @Override
    public Flight editFlightById(Long id, Flight flight) {
        Optional<Flight> flightFromDb = flightRepository.findById(id);
        if (flightFromDb.isPresent()) {
            if (!airportsExist(flight) || flight.getDepartureDate().isBefore(LocalDateTime.now()))
                return null;

            flightFromDb.get().setAirplaneModel(flight.getAirplaneModel());
            flightFromDb.get().setDepartureDate(flight.getDepartureDate());
            flightFromDb.get().setDestinationAirport(flight.getDestinationAirport());

            int numberOfBookedSeats = 0;
            Collection<Booking> bookings = flightFromDb.get().getBookedSeats();
            if (bookings.size() > 0)
                for (Booking booking : bookings)
                    numberOfBookedSeats += booking.getBookedSeatsNumber();

            if (flight.getMaxPassengersNumber() < numberOfBookedSeats)
                return null;

            flightFromDb.get().setMaxPassengersNumber(flight.getMaxPassengersNumber());
            return flightRepository.save(flightFromDb.get());
        }
        return null;
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
    public Collection<Flight> getStartedFlightsFromAirportById(long airportId) {
        return flightRepository.findByStartingAirportId(airportId);
    }

    @Override
    public Collection<Flight> getFinishedFlightsFromAirportById(long airportId) {
        return flightRepository.findByDestinationAirportId(airportId);
    }

    private boolean airportsExist (Flight flight) {
        Optional<Airport> startingAirport = airportRepository.findById(flight.getStartingAirport().getId());
        Optional<Airport> destinationAirport = airportRepository.findById(flight.getDestinationAirport().getId());

        if (!startingAirport.isPresent() || !destinationAirport.isPresent())
            return false;
        flight.setStartingAirport(startingAirport.get());
        flight.setDestinationAirport(destinationAirport.get());
        return true;
    }
}
