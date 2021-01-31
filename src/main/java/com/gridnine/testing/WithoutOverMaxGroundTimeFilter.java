package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Class represent filter without flights where ground time is more than specified duration.
 */
public final class WithoutOverMaxGroundTimeFilter implements Predicate<Flight> {

    private final Duration maxGroundDuration;

    public WithoutOverMaxGroundTimeFilter(Duration maxGroundDuration) {
        this.maxGroundDuration = maxGroundDuration;
    }

    @Override
    public boolean test(Flight flight) {
        if (flight.getSegments().size() <= 1) {
            return true;
        }
        Duration duration = Duration.ZERO;
        for (int i = 0; i <= flight.getSegments().size() - 2; i++) {
            final LocalDateTime start = flight.getSegments().get(i).getArrivalDate();
            final LocalDateTime end = flight.getSegments().get(i + 1).getDepartureDate();
            duration = duration.plus(Duration.between(start, end));
        }
        return duration.minus(maxGroundDuration).isNegative();
    }
}
