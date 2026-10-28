package pe.andeslink.andestrack.shipment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findAllByOrderByCreatedAtDesc();
    List<Incident> findByResolvedFalseOrderByCreatedAtDesc();
}
