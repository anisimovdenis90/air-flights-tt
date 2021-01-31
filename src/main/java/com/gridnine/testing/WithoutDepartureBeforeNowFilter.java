package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Class represent filter without flights where departure was made earlier the current time.
 */
public final class WithoutDepartureBeforeNowFilter implements Predicate<Flight> {

    @Override
    public boolean test(Flight flight) {
        return LocalDateTime.now().isBefore(flight.getSegments().get(0).getDepartureDate());
    }
}
