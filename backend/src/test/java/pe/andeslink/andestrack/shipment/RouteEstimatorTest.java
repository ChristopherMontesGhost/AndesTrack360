package pe.andeslink.andestrack.shipment;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
class RouteEstimatorTest {
    private final RouteEstimator estimator=new RouteEstimator();
    @Test void estimatesRoadDistanceForSupportedCities(){BigDecimal distance=estimator.distanceKm("Lima","Arequipa");assertTrue(distance.compareTo(new BigDecimal("700"))>0);assertTrue(distance.compareTo(new BigDecimal("800"))<0);}
    @Test void rejectsSameOriginAndDestination(){assertThrows(IllegalArgumentException.class,()->estimator.distanceKm("Lima","Lima"));}
    @Test void computesEtaFromDistanceSpeedAndScheduledDeparture(){LocalDateTime departure=LocalDateTime.of(2026,10,27,8,0);assertEquals(departure.plusHours(2),estimator.eta(new BigDecimal("100"),new BigDecimal("50"),departure));}
}