package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.Viaje;
import java.time.LocalDateTime;
import java.util.Locale;
public record ShipmentView(String trackingCode,String clientName,String clientEmail,String driverName,String driverEmail,String truckCode,String cargo,String origin,String destination,double distanceKm,LocalDateTime eta,String status,int progressPct,Double latitude,Double longitude,LocalDateTime updatedAt){
    public static ShipmentView from(Viaje t){var e=t.getEnvio();var r=e.getRuta();var c=e.getCliente().getUsuario();var d=t.getConductor().getUsuario();return new ShipmentView(String.format(Locale.ROOT,"AT-%04d",e.getIdEnvio()),c.getNombre(),c.getCorreo(),d.getNombre(),d.getCorreo(),t.getCamion().getPlaca(),e.getDescripcion(),r.getOrigen(),r.getDestino(),r.getDistanciaKm().doubleValue(),t.getEtaEstimada(),switch(t.getEstado()){case PROGRAMADO->"PLANNED";case EN_CURSO->"IN_TRANSIT";case DEMORADO->"DELAYED";case FINALIZADO->"DELIVERED";case CANCELADO->"CANCELLED";},t.getProgresoPct(),null,null,t.getInicioReal()==null?t.getSalidaProgramada():t.getInicioReal());}
}