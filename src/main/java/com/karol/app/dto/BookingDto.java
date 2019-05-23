package com.karol.app.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingDto implements Serializable {

    private Long id;

    @NotNull
    private LocalDateTime bookingDate;

    @NotNull
    @Min(0)
    private Integer bookedSeatsNumber;

    @NotNull
    private UserDto passenger;

    @NotNull
    private FlightDto flight;
}
