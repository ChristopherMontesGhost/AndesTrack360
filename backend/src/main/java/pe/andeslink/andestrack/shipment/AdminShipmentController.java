package pe.andeslink.andestrack.shipment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/admin/shipments")
public class AdminShipmentController {
    private final ShipmentService service;
    public AdminShipmentController(ShipmentService service){this.service=service;}
    @GetMapping public List<ShipmentView> list(){return service.all();}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ShipmentView create(@Valid @RequestBody CreateShipmentRequest request,Authentication auth){return service.create(request,auth.getName());}
}