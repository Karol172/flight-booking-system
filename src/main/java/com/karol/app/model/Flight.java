package com.karol.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime departureDate;

    @Column(nullable = false)
    private String airplaneModel;

    @Column(nullable = false)
    private Integer maxPassengersNumber;

    @OneToMany(mappedBy = "flight", orphanRemoval = true)
    private Collection<Booking> bookedSeats;

    @ManyToOne(optional = false)
    @JoinColumn(name = "starting_airport_id", referencedColumnName = "id")
    private Airport startingAirport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_airport_id", referencedColumnName = "id")
    private Airport destinationAirport;

    public Flight(LocalDateTime departureDate, String airplaneModel, Integer maxPassengersNumber, Airport startingAirport, Airport destinationAirport) {
        this.departureDate = departureDate;
        this.airplaneModel = airplaneModel;
        this.maxPassengersNumber = maxPassengersNumber;
        this.startingAirport = startingAirport;
        this.destinationAirport = destinationAirport;
    }
}
