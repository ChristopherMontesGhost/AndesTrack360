package pe.andeslink.andestrack.shipment;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
@RestController @RequestMapping("/api/control")
public class ControlController {
    private final ShipmentService shipments;private final IncidentService incidents;
    public ControlController(ShipmentService shipments,IncidentService incidents){this.shipments=shipments;this.incidents=incidents;}
    @GetMapping("/shipments") public List<ShipmentView> shipments(){return shipments.all();}
    @GetMapping("/incidents") public List<IncidentView> incidents(){return incidents.activeIncidents();}
    @GetMapping("/alerts") public List<AlertView> alerts(){return incidents.activeAlerts();}
    @PatchMapping("/alerts/{id}/resolve") public AlertView resolve(@PathVariable int id,@Valid @RequestBody(required=false) ResolveAlertRequest request,Authentication auth){return incidents.resolve(id,auth.getName(),request!=null&&request.discard());}
}