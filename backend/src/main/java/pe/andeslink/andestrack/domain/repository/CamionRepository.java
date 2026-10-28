package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Camion;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoCamion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CamionRepository extends JpaRepository<Camion,Integer>{Optional<Camion> findByPlacaIgnoreCase(String placa);List<Camion> findByEstadoOrderByPlaca(EstadoCamion estado);}