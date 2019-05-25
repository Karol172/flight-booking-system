package com.karol.app.service;

import com.karol.app.model.Airport;
import com.karol.app.repository.AirportRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportServiceImplTest {

    @Autowired
    private AirportRepository airportRepository;

    private AirportServiceImpl airportService;

    @Before
    public void setUp() {
        airportService = new AirportServiceImpl(airportRepository);
    }

    @Test
    public void shouldReturnAllAirports () {
        List<Airport> airportsFromService = new ArrayList<>(airportService.getAllAirports());
        List<Airport> airportsFromRepository = new ArrayList<>(airportRepository.findAll());

        assertEquals(airportsFromRepository.size(), airportsFromService.size());
        for (int i=0; i<airportsFromRepository.size(); i++) {
            assertEquals(airportsFromService.get(i).getId(), airportsFromRepository.get(i).getId());
            assertEquals(airportsFromService.get(i).getCity(), airportsFromRepository.get(i).getCity());
            assertEquals(airportsFromService.get(i).getCountry(), airportsFromRepository.get(i).getCountry());
        }
    }

    @Test
    public void shouldReturnSortedAirport () {
        int records = 4;
        List<Airport> airportsFromRepository = new ArrayList<>(airportRepository.findAll());
        airportsFromRepository.sort((airport, t1) -> airport.getCity().compareTo(t1.getCity()));

        Page<Airport> airportPage = airportService.getAllAirportsSortedBy("city", 0, records);
        List<Airport> results = airportPage.get().collect(Collectors.toList());

        for (int i=0; i<records; i++) {
            assertEquals(results.get(i).getCountry(), airportsFromRepository.get(i).getCountry());
            assertEquals(results.get(i).getCity(), airportsFromRepository.get(i).getCity());
        }
    }

    @Test
    public void shouldReturnAirport () {
        Optional<Airport> airportFromRepository = airportRepository.findById(1L);
        if (airportFromRepository.isPresent()) {
            Airport airportFromService = airportService.getAirportById(1L);
            assertEquals(airportFromRepository.get().getId(), airportFromService.getId());
            assertEquals(airportFromRepository.get().getCity(), airportFromService.getCity());
            assertEquals(airportFromRepository.get().getCountry(), airportFromService.getCountry());
        }
    }

    @Test
    public void shouldCreateAirport () {
        Airport airport = airportService.createAirport(new Airport("Toronto", "Canada"));
        Airport airportFromService = airportService.getAirportById(airport.getId());
        assertEquals(airport.getCountry(), airportFromService.getCountry());
        assertEquals(airport.getCity(), airportFromService.getCity());
    }

    @Test
    public void shouldUpdateAirport () {
        Airport airport = new Airport( "Toronto", "Canada");
        airportService.editAirportById(1L, airport);
        Airport airportFromService = airportService.getAirportById(1L);
        assertEquals(airportFromService.getCountry(), airport.getCountry());
        assertEquals(airportFromService.getCity(), airport.getCity());
    }

    @Test
    public void shouldDeleteAirport () {
        if (airportRepository.findById(1L).isPresent()) {
            assertTrue(airportService.removeAirportById(1L));
            assertFalse(airportRepository.findById(1L).isPresent());
        }
    }

}
