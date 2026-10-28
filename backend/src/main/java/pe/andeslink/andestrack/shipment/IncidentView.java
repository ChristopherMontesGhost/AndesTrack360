package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.Incidencia;
import java.time.LocalDateTime;
import java.util.Locale;
public record IncidentView(Integer id,String trackingCode,String truckCode,String type,String severity,String description,boolean resolved,LocalDateTime createdAt){
    public static IncidentView from(Incidencia i){var a=i.getAlerta();return new IncidentView(i.getIdIncidencia(),String.format(Locale.ROOT,"AT-%04d",i.getViaje().getEnvio().getIdEnvio()),i.getViaje().getCamion().getPlaca(),i.getTipo().name(),a==null?"LOW":a.getSeveridad().name(),i.getDescripcion(),i.getEstado()!=pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoIncidencia.PENDIENTE&&i.getEstado()!=pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoIncidencia.EN_GESTION,i.getFechaHora());}
}