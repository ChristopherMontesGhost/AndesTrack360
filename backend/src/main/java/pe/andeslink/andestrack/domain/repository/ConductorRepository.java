package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ConductorRepository extends JpaRepository<Conductor,Integer>{Optional<Conductor> findByUsuarioCorreoIgnoreCase(String correo);}