package org.example.flight_booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.flight_booking.dto.AircraftInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AircraftDataService {
    private static final Logger log = LoggerFactory.getLogger(AircraftDataService.class);
    private final Map<String, AircraftInfo> aircraftData = new LinkedHashMap<>();

    @PostConstruct
    public void loadAircraftData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("aircraft-data.json")) {
            if (inputStream == null) {
                log.error("aircraft-data.json file not found!");
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, AircraftInfo> rawData = objectMapper.readValue(inputStream, new TypeReference<>() {});

            rawData.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(entry -> entry.getValue().maxFlightTime()))
                    .forEachOrdered(entry -> aircraftData.put(entry.getKey(), entry.getValue()));

            log.info("Loaded {} aircraft types from JSON.", aircraftData.size());
        } catch (Exception e) {
            log.error("Failed to load aircraft data: {}", e.getMessage());
        }
    }

    public boolean isValidAircraftType(String aircraftType) {
        return aircraftData.containsKey(aircraftType);
    }

    public String getSeatPlanFile(String aircraftType) {
        return aircraftData.get(aircraftType).seatPlanFile();
    }

    public String findBestAircraft(int durationMinutes) {
        for (Map.Entry<String, AircraftInfo> entry : aircraftData.entrySet()) {
            if (entry.getValue().maxFlightTime() >= durationMinutes) {
                return entry.getKey();
            }
        }

        return aircraftData.keySet().stream().toList().getLast();
    }
}
