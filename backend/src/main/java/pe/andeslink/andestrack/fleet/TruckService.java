package pe.andeslink.andestrack.fleet;
import pe.andeslink.andestrack.domain.entity.Camion;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoCamion;
import pe.andeslink.andestrack.domain.repository.CamionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class TruckService {
    private final CamionRepository trucks;
    public TruckService(CamionRepository trucks){this.trucks=trucks;}
    @Transactional public TruckView create(CreateTruckRequest r){String plate=StringUtils.upperCase(StringUtils.deleteWhitespace(r.plate()));if(trucks.findByPlacaIgnoreCase(plate).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"La placa ya está registrada");return TruckView.from(trucks.save(new Camion(plate,StringUtils.trim(r.model()),r.capacityTon(),EstadoCamion.DISPONIBLE)));}
    @Transactional(readOnly=true) public List<TruckView> all(){return trucks.findAll().stream().map(TruckView::from).toList();}
}