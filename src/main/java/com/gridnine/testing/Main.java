package com.gridnine.testing;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) {
        FlightFilterBuilder.builder()
                .addFilter(new WithoutDepartureBeforeNowFilter())
                .print(FlightBuilder.createFlights());

        FlightFilterBuilder.builder()
                .addFilter(new WithoutArrivalBeforeDepartureFilter())
                .print(FlightBuilder.createFlights());

        FlightFilterBuilder.builder()
                .addFilter(new WithoutOverMaxGroundTimeFilter(Duration.of(2, ChronoUnit.HOURS)))
                .print(FlightBuilder.createFlights());

    }
}
