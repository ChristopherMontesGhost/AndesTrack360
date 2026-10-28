package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.*;
import pe.andeslink.andestrack.domain.repository.*;
import pe.andeslink.andestrack.realtime.RealtimePublisher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
/** Records driver incidents and creates the linked alert in one transaction. */
@Service
public class IncidentService {
    private static final Logger log=LoggerFactory.getLogger(IncidentService.class);
    private final ViajeRepository viajes;private final IncidenciaRepository incidencias;private final AlertaRepository alertas;private final UsuarioRepository usuarios;private final EventoViajeRepository eventos;private final EnvioRepository envios;private final CamionRepository camiones;private final RealtimePublisher realtime;
    public IncidentService(ViajeRepository viajes,IncidenciaRepository incidencias,AlertaRepository alertas,UsuarioRepository usuarios,EventoViajeRepository eventos,EnvioRepository envios,CamionRepository camiones,RealtimePublisher realtime){this.viajes=viajes;this.incidencias=incidencias;this.alertas=alertas;this.usuarios=usuarios;this.eventos=eventos;this.envios=envios;this.camiones=camiones;this.realtime=realtime;}
    @Transactional public IncidentView report(String code,String driverEmail,IncidentRequest request){Viaje trip=tripFor(code,driverEmail);if(trip.getEstado()!=EstadoViaje.EN_CURSO&&trip.getEstado()!=EstadoViaje.DEMORADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"El viaje debe estar iniciado para reportar una incidencia");String description=StringUtils.normalizeSpace(request.description());LocalDateTime now=LocalDateTime.now();Incidencia incident=incidencias.save(new Incidencia(trip,request.type(),description,now));Alerta alert=alertas.save(new Alerta(incident,severity(request.type()),now));if(request.type()==TipoIncidencia.FALLA_MECANICA||request.type()==TipoIncidencia.ACCIDENTE){trip.demorar();trip.getEnvio().actualizarEstado(EstadoEnvio.DEMORADO);viajes.save(trip);envios.save(trip.getEnvio());camiones.save(trip.getCamion());}eventos.save(new EventoViaje(trip,"INCIDENCIA_REPORTADA",description,now));ShipmentView shipment=ShipmentMapper.toView(trip);IncidentView view=ShipmentMapper.toIncidentView(incident);AlertView alertView=ShipmentMapper.toAlertView(alert);realtime.incidentReported(shipment,view,alertView);log.info("Incident {} recorded for {}",incident.getIdIncidencia(),code);return view;}
    @Transactional(readOnly=true) public List<AlertView> activeAlerts(){return alertas.findByEstadoOrderByFechaHoraDesc(EstadoAlerta.ACTIVA).stream().map(ShipmentMapper::toAlertView).toList();}
    @Transactional(readOnly=true) public List<IncidentView> activeIncidents(){return incidencias.findByEstadoInOrderByFechaHoraDesc(List.of(EstadoIncidencia.PENDIENTE,EstadoIncidencia.EN_GESTION)).stream().map(ShipmentMapper::toIncidentView).toList();}
    @Transactional public AlertView resolve(int id,String coordinatorEmail,boolean discard){Alerta alert=alertas.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Alerta no encontrada"));if(alert.getEstado()!=EstadoAlerta.ACTIVA)throw new ResponseStatusException(HttpStatus.CONFLICT,"La alerta ya fue atendida");Usuario user=usuarios.findByCorreoIgnoreCase(coordinatorEmail).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));alert.atender(user,discard?EstadoAlerta.DESCARTADA:EstadoAlerta.REVISADA);Alerta saved=alertas.save(alert);AlertView view=ShipmentMapper.toAlertView(saved);realtime.alertResolved(view);return view;}
    private Viaje tripFor(String code,String email){return viajes.findFirstByEnvioIdEnvioAndConductorUsuarioCorreoIgnoreCase(ShipmentCodes.idFromTrackingCode(code),email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Viaje no encontrado"));}
    private SeveridadAlerta severity(TipoIncidencia type){return switch(type){case ACCIDENTE->SeveridadAlerta.CRITICAL;case FALLA_MECANICA->SeveridadAlerta.HIGH;case CLIMA_ADVERSO,BLOQUEO->SeveridadAlerta.MEDIUM;case TRAFICO_INTENSO,OTRO->SeveridadAlerta.LOW;};}
}