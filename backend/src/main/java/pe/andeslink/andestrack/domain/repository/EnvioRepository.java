package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EnvioRepository extends JpaRepository<Envio,Integer>{List<Envio> findAllByOrderByIdEnvioDesc();List<Envio> findByClienteUsuarioCorreoIgnoreCaseOrderByIdEnvioDesc(String correo);}