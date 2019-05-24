package com.karol.app.service;

import com.karol.app.repository.AirportRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class AirportServiceImplTest {

    @Mock
    private AirportRepository airportRepository;

    private AirportServiceImpl airportService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        airportService = new AirportServiceImpl(airportRepository);
    }

    @Test
    public void shouldReturnAllAirports () {

        assertEquals(airportRepository.findAll(), airportService.getAllAirports());
    }
}
