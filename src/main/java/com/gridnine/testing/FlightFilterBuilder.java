package com.gridnine.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Factory class to get filtered list of flights. Based on stream API.
 */
public class FlightFilterBuilder {

    private List<Predicate<Flight>> predicates;

    public FlightFilterBuilder() {
        this.predicates = new ArrayList<>();
    }

    public static FlightFilterBuilder builder() {
        return new FlightFilterBuilder();
    }

    public FlightFilterBuilder addFilter(Predicate<Flight> predicate) {
        predicates.add(predicate);
        return this;
    }

    public FlightFilterBuilder addFilters(Collection<Predicate<Flight>> predicates) {
        this.predicates.addAll(predicates);
        return this;
    }

    public List<Flight> filter(Collection<Flight> flights) {
        return flights.stream().filter(predicates.stream().reduce(x -> true, Predicate::and)).collect(Collectors.toList());
    }

    public List<Flight> filterParallel(Collection<Flight> flights) {
        return flights.stream().parallel().filter(predicates.stream().reduce(x -> true, Predicate::and)).collect(Collectors.toList());
    }

    public void print(Collection<Flight> flights) {
        System.out.println("----START PRINT----");
        flights.stream().filter(predicates.stream().reduce(x -> true, Predicate::and)).forEach(System.out::println);
        System.out.println("-----END PRINT-----");
    }
}
