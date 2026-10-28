package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Viaje;
import pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ViajeRepository extends JpaRepository<Viaje,Integer>{List<Viaje> findAllByOrderByIdViajeDesc();List<Viaje> findByConductorUsuarioCorreoIgnoreCaseOrderByIdViajeDesc(String correo);List<Viaje> findByEnvioClienteUsuarioCorreoIgnoreCaseOrderByIdViajeDesc(String correo);Optional<Viaje> findFirstByEnvioIdEnvioAndConductorUsuarioCorreoIgnoreCase(Integer idEnvio,String correo);Optional<Viaje> findFirstByEnvioIdEnvioOrderByIdViajeDesc(Integer idEnvio);}