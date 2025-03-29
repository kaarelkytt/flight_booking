package org.example.flight_booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class IataCityService {
    private static final Logger log = LoggerFactory.getLogger(IataCityService.class);
    private final Map<String, String> iataCityMap = new HashMap<>();

    /**
     * Load IATA city data from JSON file.
     */
    @PostConstruct
    public void loadIataCityData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("iata-cities.json")) {
            if (inputStream == null) {
                log.error("iata-cities.json file not found!");
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            iataCityMap.putAll(objectMapper.readValue(inputStream, new TypeReference<>() {}));

            log.info("Loaded {} IATA city mappings.", iataCityMap.size());
        } catch (Exception e) {
            log.error("Failed to load IATA city mappings: {}", e.getMessage());
        }
    }

    /**
     * Get the city name for an IATA code.
     *
     * @param iataCode the IATA code
     * @return the city name
     */
    public String getCityName(String iataCode) {
        String cityName = iataCityMap.getOrDefault(iataCode, "UNKNOWN");
        if ("UNKNOWN".equals(cityName)) {
            log.warn("City name for IATA code {} not found.", iataCode);
        }
        return cityName;
    }
}
