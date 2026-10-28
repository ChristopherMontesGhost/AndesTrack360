package pe.andeslink.andestrack.security;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin/catalog")
public class AdminCatalogController {
    private final AdminCatalogService catalog;
    public AdminCatalogController(AdminCatalogService catalog){this.catalog=catalog;}
    @GetMapping public AdminCatalogView get(){return catalog.get();}
}