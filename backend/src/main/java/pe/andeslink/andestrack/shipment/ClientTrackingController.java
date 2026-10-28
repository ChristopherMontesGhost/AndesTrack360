package pe.andeslink.andestrack.shipment;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/client/tracking")
public class ClientTrackingController {
    private final ShipmentService shipments;
    public ClientTrackingController(ShipmentService shipments){this.shipments=shipments;}
    @GetMapping public List<ShipmentView> list(Authentication auth){return shipments.forClient(auth.getName());}
    @GetMapping("/{code}") public ShipmentView tracking(@PathVariable String code,Authentication auth){return shipments.forClient(code,auth.getName());}
}