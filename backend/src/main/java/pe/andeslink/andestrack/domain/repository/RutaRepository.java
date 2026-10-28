package pe.andeslink.andestrack.domain.repository;
import pe.andeslink.andestrack.domain.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RutaRepository extends JpaRepository<Ruta,Integer>{Optional<Ruta> findFirstByOrigenIgnoreCaseAndDestinoIgnoreCase(String origen,String destino);}