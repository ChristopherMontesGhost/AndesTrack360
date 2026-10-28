package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ClienteRepository extends JpaRepository<Cliente,Integer>{Optional<Cliente> findByUsuarioCorreoIgnoreCase(String correo);}