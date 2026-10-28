package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.Alerta;
import java.time.LocalDateTime;
import java.util.Locale;
public record AlertView(Integer id,String type,String severity,String text,String truck,String time,boolean open,LocalDateTime createdAt){
    public static AlertView from(Alerta a){var i=a.getIncidencia();var v=i.getViaje();return new AlertView(a.getIdAlerta(),i.getTipo().name(),a.getSeveridad().name(),i.getDescripcion(),v.getCamion().getPlaca(),a.getFechaHora().toString(),a.getEstado()==pe.andeslink.andestrack.domain.entity.DomainEnums.EstadoAlerta.ACTIVA,a.getFechaHora());}
}