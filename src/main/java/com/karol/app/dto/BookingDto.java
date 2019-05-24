package com.karol.app.dto;

import com.karol.app.validator.NotNullId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingDto implements Serializable {

    private Long id;

    private LocalDateTime bookingDate;

    @NotNull
    @Min(1)
    private Integer bookedSeatsNumber;

    private UserDto passenger;

    @NotNull
    @NotNullId
    private FlightDto flight;
}
