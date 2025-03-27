package org.example.flight_booking.specification;

import jakarta.persistence.criteria.*;
import org.example.flight_booking.model.Flight;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightSpecification {
    public static Specification<Flight> withFilters(LocalDate startDate, LocalDate endDate,
                                                    String departure, String destination,
                                                    Integer minDuration, Integer maxDuration,
                                                    Double minPrice, Double maxPrice) {
        return (Root<Flight> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("departureTime"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("departureTime"), endDate.atTime(23, 59, 59)));
            }
            if (departure != null) {
                predicates.add(cb.equal(root.get("departureIATA"), departure));
            }
            if (destination != null) {
                predicates.add(cb.equal(root.get("destinationIATA"), destination));
            }
            if (minDuration != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("durationMinutes"), minDuration));
            }
            if (maxDuration != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("durationMinutes"), maxDuration));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("initialPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("initialPrice"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
