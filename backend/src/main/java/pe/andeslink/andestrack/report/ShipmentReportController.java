package pe.andeslink.andestrack.report;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
@RestController @RequestMapping("/api/admin/reports")
public class ShipmentReportController {
    private final ShipmentReportService reports;
    public ShipmentReportController(ShipmentReportService reports){this.reports=reports;}
    @GetMapping("/shipments.xlsx") public ResponseEntity<byte[]> shipments()throws IOException{return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=andestrack-envios.xlsx").body(reports.export());}
}