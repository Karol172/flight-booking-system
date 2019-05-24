package com.karol.app.repository;

import com.karol.app.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Collection<Flight> findByDestinationAirportId(long id);

    Collection<Flight> findByStartingAirportId(long id);

}
