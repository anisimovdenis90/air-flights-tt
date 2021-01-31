package com.gridnine.testing;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class FlightFilterBuilderTest {

    private static List<Flight> normalFlights;
    private static List<Flight> badFlights;
    private static Flight badFlightDepartureInPast;
    private static Flight badFlightDepartureBeforeArrival;
    private static Flight badFlightOverMaxGroundTime;

    @BeforeClass
    public static void init() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);

        normalFlights = Collections.singletonList(new Flight(Arrays.asList(new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2)))));

        badFlightDepartureInPast = new Flight(Arrays.asList(new Segment(threeDaysFromNow.minusDays(6), threeDaysFromNow)));
        badFlightDepartureBeforeArrival = new Flight(Arrays.asList(new Segment(threeDaysFromNow, threeDaysFromNow.minusHours(6))));
        badFlightOverMaxGroundTime = new Flight(Arrays.asList(
                new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                new Segment(threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)))
        );

        badFlights = Arrays.asList(badFlightDepartureInPast, badFlightDepartureBeforeArrival, badFlightOverMaxGroundTime);
    }

    @Test
    public void withoutDepartureBeforeNowFilter() {
        FlightFilterBuilder filter = FlightFilterBuilder.builder()
                .addFilter(new WithoutDepartureBeforeNowFilter());

        List<Flight> badList = filter.filter(badFlights);
        normalFlights = filter.filter(normalFlights);

        Assert.assertEquals(2, badList.size());
        Assert.assertFalse(badList.contains(badFlightDepartureInPast));
        Assert.assertEquals(1, normalFlights.size());
    }

    @Test
    public void withoutArrivalBeforeDepartureFilterTest() {
        FlightFilterBuilder filter = FlightFilterBuilder.builder()
                .addFilter(new WithoutArrivalBeforeDepartureFilter());

        List<Flight> badList = filter.filter(badFlights);
        normalFlights = filter.filter(normalFlights);

        Assert.assertEquals(2, badList.size());
        Assert.assertFalse(badList.contains(badFlightDepartureBeforeArrival));
        Assert.assertEquals(1, normalFlights.size());
    }


    @Test
    public void withoutOverMaxGroundTimeTest() {
        FlightFilterBuilder filter = FlightFilterBuilder.builder()
                .addFilter(new WithoutOverMaxGroundTimeFilter(Duration.of(2, ChronoUnit.HOURS)));

        List<Flight> badList = filter.filter(badFlights);
        normalFlights = filter.filter(normalFlights);

        Assert.assertEquals(2, badList.size());
        Assert.assertFalse(badList.contains(badFlightOverMaxGroundTime));
        Assert.assertEquals(1, normalFlights.size());
    }

    @Test
    public void allFiltersTest() {
        List<Predicate<Flight>> predicates = new ArrayList<>(Arrays.asList(
                new WithoutArrivalBeforeDepartureFilter(),
                new WithoutDepartureBeforeNowFilter(),
                new WithoutOverMaxGroundTimeFilter(Duration.of(2, ChronoUnit.HOURS)))
        );

        FlightFilterBuilder filter = FlightFilterBuilder.builder().addFilters(predicates);

        List<Flight> badList = filter.filter(badFlights);
        normalFlights = filter.filter(normalFlights);
        Assert.assertEquals(0, badList.size());
        Assert.assertFalse(badList.containsAll(Arrays.asList(badFlightDepartureBeforeArrival, badFlightDepartureInPast, badFlightOverMaxGroundTime)));
        Assert.assertEquals(1, normalFlights.size());
    }
}
