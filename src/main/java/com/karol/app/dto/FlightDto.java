package com.karol.app.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FlightDto implements Serializable {

    private Long id;

    @NotNull
    private LocalDateTime departureDate;

    @NotNull
    private String airplaneModel;

    @NotNull
    @Min(0)
    private Integer maxPassengersNumber;

    @NotNull
    private AirportDto destinationAirport;
}
