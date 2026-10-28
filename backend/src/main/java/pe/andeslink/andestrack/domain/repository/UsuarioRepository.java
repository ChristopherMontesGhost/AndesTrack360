package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/** DAO for Usuario records. */
public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{Optional<Usuario> findByCorreoIgnoreCase(String correo);}