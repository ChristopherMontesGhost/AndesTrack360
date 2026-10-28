package pe.andeslink.andestrack.fleet;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/admin/trucks")
public class TruckAdminController {
    private final TruckService service;
    public TruckAdminController(TruckService service){this.service=service;}
    @GetMapping public List<TruckView> list(){return service.all();}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public TruckView create(@Valid @RequestBody CreateTruckRequest r){return service.create(r);}
}