package pe.andeslink.andestrack.shipment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByTrackingCode(String trackingCode);
    List<Shipment> findAllByOrderByUpdatedAtDesc();
    List<Shipment> findByDriverEmailIgnoreCaseOrderByUpdatedAtDesc(String email);
    List<Shipment> findByClientEmailIgnoreCaseOrderByUpdatedAtDesc(String email);
    long countByStatus(ShipmentStatus status);
}
