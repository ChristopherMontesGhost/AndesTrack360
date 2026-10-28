package pe.andeslink.andestrack.shipment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="incident", indexes=@Index(name="idx_incident_opened", columnList="resolved,created_at"))
public class Incident {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="tracking_code", length=20) private String trackingCode;
    @Column(name="truck_code", nullable=false, length=20) private String truckCode;
    @Column(name="incident_type", nullable=false, length=24) private String type;
    @Column(nullable=false, length=20) private String severity;
    @Column(nullable=false, length=500) private String description;
    @Column(nullable=false) private boolean resolved;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    protected Incident() {}
    Incident(String trackingCode, String truckCode, String type, String severity, String description) {
        this.trackingCode=trackingCode; this.truckCode=truckCode; this.type=type;
        this.severity=severity; this.description=description; this.createdAt=Instant.now();
    }
    public Long getId(){return id;} public String getTrackingCode(){return trackingCode;}
    public String getTruckCode(){return truckCode;} public String getType(){return type;}
    public String getSeverity(){return severity;} public String getDescription(){return description;}
    public boolean isResolved(){return resolved;} public Instant getCreatedAt(){return createdAt;}
    public void resolve(){this.resolved=true;}
}
