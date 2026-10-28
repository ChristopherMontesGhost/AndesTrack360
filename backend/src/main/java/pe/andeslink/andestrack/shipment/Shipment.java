package pe.andeslink.andestrack.shipment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "shipment", indexes = {
        @Index(name = "idx_shipment_client", columnList = "client_email"),
        @Index(name = "idx_shipment_driver", columnList = "driver_email"),
        @Index(name = "idx_shipment_status", columnList = "status")})
public class Shipment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tracking_code", nullable = false, unique = true, length = 20)
    private String trackingCode;
    @Column(name = "client_name", nullable = false, length = 120) private String clientName;
    @Column(name = "client_email", nullable = false, length = 190) private String clientEmail;
    @Column(name = "driver_name", nullable = false, length = 120) private String driverName;
    @Column(name = "driver_email", nullable = false, length = 190) private String driverEmail;
    @Column(name = "truck_code", nullable = false, length = 20) private String truckCode;
    @Column(nullable = false, length = 160) private String cargo;
    @Column(nullable = false, length = 80) private String origin;
    @Column(nullable = false, length = 80) private String destination;
    @Column(name = "distance_km", nullable = false) private double distanceKm;
    @Column(nullable = false) private Instant eta;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 24) private ShipmentStatus status;
    @Column(name = "progress_pct", nullable = false) private int progressPct;
    private Double latitude;
    private Double longitude;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    protected Shipment() {}
    public Long getId() { return id; }
    public String getTrackingCode() { return trackingCode; }
    public String getClientName() { return clientName; }
    public String getClientEmail() { return clientEmail; }
    public String getDriverName() { return driverName; }
    public String getDriverEmail() { return driverEmail; }
    public String getTruckCode() { return truckCode; }
    public String getCargo() { return cargo; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public double getDistanceKm() { return distanceKm; }
    public Instant getEta() { return eta; }
    public ShipmentStatus getStatus() { return status; }
    public int getProgressPct() { return progressPct; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Instant getUpdatedAt() { return updatedAt; }
    void setTrackingCode(String v) { trackingCode=v; }
    void setClientName(String v) { clientName=v; }
    void setClientEmail(String v) { clientEmail=v; }
    void setDriverName(String v) { driverName=v; }
    void setDriverEmail(String v) { driverEmail=v; }
    void setTruckCode(String v) { truckCode=v; }
    void setCargo(String v) { cargo=v; }
    void setOrigin(String v) { origin=v; }
    void setDestination(String v) { destination=v; }
    void setDistanceKm(double v) { distanceKm=v; }
    void setEta(Instant v) { eta=v; }
    void setStatus(ShipmentStatus v) { status=v; }
    void setProgressPct(int v) { progressPct=v; }
    void setLatitude(Double v) { latitude=v; }
    void setLongitude(Double v) { longitude=v; }
    void setUpdatedAt(Instant v) { updatedAt=v; }
}
