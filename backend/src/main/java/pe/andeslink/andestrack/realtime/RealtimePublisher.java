package pe.andeslink.andestrack.realtime;
import pe.andeslink.andestrack.shipment.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.time.Instant;
/** Publishes committed domain changes to role-protected STOMP destinations. */
@Component
public class RealtimePublisher {
    private final SimpMessagingTemplate messaging;
    public RealtimePublisher(SimpMessagingTemplate messaging){this.messaging=messaging;}
    public void shipmentChanged(String event,ShipmentView shipment){var payload=new ShipmentEvent(event,shipment,null,null,Instant.now());messaging.convertAndSend("/topic/control/shipments",payload);messaging.convertAndSendToUser(shipment.clientEmail(),"/queue/tracking",payload);messaging.convertAndSendToUser(shipment.driverEmail(),"/queue/driver",payload);}
    public void incidentReported(ShipmentView shipment,IncidentView incident,AlertView alert){var payload=new ShipmentEvent("INCIDENT_REPORTED",shipment,incident,alert,Instant.now());messaging.convertAndSend("/topic/control/alerts",payload);messaging.convertAndSend("/topic/control/shipments",payload);messaging.convertAndSendToUser(shipment.clientEmail(),"/queue/tracking",payload);messaging.convertAndSendToUser(shipment.driverEmail(),"/queue/driver",payload);}
    public void alertResolved(AlertView alert){var payload=new ShipmentEvent("ALERT_RESOLVED",null,null,alert,Instant.now());messaging.convertAndSend("/topic/control/alerts",payload);}
}