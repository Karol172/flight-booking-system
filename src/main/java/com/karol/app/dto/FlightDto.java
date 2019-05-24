package com.karol.app.dto;

import com.karol.app.validator.NotNullId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FlightDto implements Serializable {

    private Long id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future
    private LocalDateTime departureDate;

    @NotNull
    private String airplaneModel;

    @NotNull
    @Min(1)
    private Integer maxPassengersNumber;

    @NotNull
    @NotNullId
    private AirportDto startingAirport;

    @NotNull
    @NotNullId
    private AirportDto destinationAirport;

}
