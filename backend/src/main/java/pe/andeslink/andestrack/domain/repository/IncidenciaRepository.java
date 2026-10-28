package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Incidencia;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface IncidenciaRepository extends JpaRepository<Incidencia,Integer>{List<Incidencia> findAllByOrderByFechaHoraDesc();List<Incidencia> findByEstadoInOrderByFechaHoraDesc(List<EstadoIncidencia> estados);}