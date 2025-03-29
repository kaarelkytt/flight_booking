package org.example.flight_booking.service;

import org.example.flight_booking.dto.FlightData;
import org.example.flight_booking.dto.FlightResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FlightApiService {
    private static final Logger log = LoggerFactory.getLogger(FlightApiService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.aviationstack.url}")
    private String apiUrl;

    @Value("${api.aviationstack.key}")
    private String apiKey;

    @Value("#{'${api.aviationstack.departure.iata}'.split(',')}")
    private List<String> departureIataList;

    /**
     * Fetches flights from the API by sending a request to the API for each departure IATA code.
     *
     * @return the list of flights
     */
    public List<FlightData> fetchFlights() {
        List<FlightData> flights = new java.util.ArrayList<>(List.of());
        for (String departureIata : departureIataList) {
            String requestUrl = apiUrl + "?access_key=" + apiKey + "&dep_iata=" + departureIata;
            log.info("Fetching flights from API: {}", requestUrl);

            FlightResponse response = restTemplate.getForObject(requestUrl, FlightResponse.class);

            if (response != null && response.getData() != null) {
                log.info("Received {} flights from API.", response.getData().size());
                flights.addAll(response.getData());
            } else {
                log.warn("No data received from API.");
            }
        }
        return flights;
    }
}
