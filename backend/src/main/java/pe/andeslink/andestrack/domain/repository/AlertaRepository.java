package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Alerta;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AlertaRepository extends JpaRepository<Alerta,Integer>{List<Alerta> findByEstadoOrderByFechaHoraDesc(EstadoAlerta estado);}