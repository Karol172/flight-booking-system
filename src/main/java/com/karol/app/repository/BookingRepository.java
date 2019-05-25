package com.karol.app.repository;

import com.karol.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByPassengerId (long id);

    Collection<Booking> findByFlightId (long id);

}
