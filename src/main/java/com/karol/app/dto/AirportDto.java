package com.karol.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AirportDto implements Serializable {

    private Long id;

    @NotNull
    private String city;

    @NotNull
    private String country;

}
