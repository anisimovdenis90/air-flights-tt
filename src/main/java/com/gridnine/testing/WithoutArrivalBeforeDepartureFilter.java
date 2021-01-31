package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Class represent filter without flights where arrival date is earlier the departure.
 */
public final class WithoutArrivalBeforeDepartureFilter implements Predicate<Flight> {

    @Override
    public boolean test(Flight flight) {
        final LocalDateTime departureDate = flight.getSegments().get(0).getDepartureDate();
        final LocalDateTime arrivalDate = flight.getSegments().get(flight.getSegments().size() - 1).getArrivalDate();
        return departureDate.isBefore(arrivalDate);
    }
}
