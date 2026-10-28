package pe.andeslink.andestrack.realtime;
import pe.andeslink.andestrack.shipment.*;
import java.time.Instant;
public record ShipmentEvent(String type,ShipmentView shipment,IncidentView incident,AlertView alert,Instant occurredAt){}