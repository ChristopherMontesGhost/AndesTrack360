package pe.andeslink.andestrack.shipment;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/driver")
public class DriverController {
    private final TripService trips;private final IncidentService incidents;
    public DriverController(TripService trips,IncidentService incidents){this.trips=trips;this.incidents=incidents;}
    @GetMapping("/shipments") public List<ShipmentView> assigned(Authentication auth){return trips.assigned(auth.getName());}
    @PostMapping("/shipments/{code}/start") public ShipmentView start(@PathVariable String code,Authentication auth){return trips.start(code,auth.getName());}
    @PostMapping("/shipments/{code}/incidents") public IncidentView report(@PathVariable String code,@Valid @RequestBody IncidentRequest request,Authentication auth){return incidents.report(code,auth.getName(),request);}
}