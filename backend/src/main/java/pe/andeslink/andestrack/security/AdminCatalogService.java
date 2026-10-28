package pe.andeslink.andestrack.security;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoCamion;
import pe.andeslink.andestrack.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AdminCatalogService {
    private final ClienteRepository clients;private final ConductorRepository drivers;private final CamionRepository trucks;
    public AdminCatalogService(ClienteRepository clients,ConductorRepository drivers,CamionRepository trucks){this.clients=clients;this.drivers=drivers;this.trucks=trucks;}
    @Transactional(readOnly=true) public AdminCatalogView get(){var cs=clients.findAll().stream().map(c->new AdminCatalogView.AccountOption(c.getUsuario().getCorreo(),c.getUsuario().getNombre())).toList();var ds=drivers.findAll().stream().map(d->new AdminCatalogView.AccountOption(d.getUsuario().getCorreo(),d.getUsuario().getNombre())).toList();var ts=trucks.findByEstadoOrderByPlaca(EstadoCamion.DISPONIBLE).stream().map(t->new AdminCatalogView.TruckOption(t.getPlaca(),t.getModelo(),t.getCapacidadTon().doubleValue(),t.getEstado().name())).toList();return new AdminCatalogView(cs,ds,ts);}
}