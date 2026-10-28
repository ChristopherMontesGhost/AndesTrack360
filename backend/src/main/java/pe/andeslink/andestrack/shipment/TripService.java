package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.*;
import pe.andeslink.andestrack.domain.repository.*;
import pe.andeslink.andestrack.realtime.RealtimePublisher;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
/** Driver-facing use cases for starting and viewing assigned trips. */
@Service
public class TripService {
    private static final Logger log=LoggerFactory.getLogger(TripService.class);
    private final ViajeRepository viajes;private final EnvioRepository envios;private final CamionRepository camiones;private final EventoViajeRepository eventos;private final RealtimePublisher realtime;
    public TripService(ViajeRepository viajes,EnvioRepository envios,CamionRepository camiones,EventoViajeRepository eventos,RealtimePublisher realtime){this.viajes=viajes;this.envios=envios;this.camiones=camiones;this.eventos=eventos;this.realtime=realtime;}
    @Transactional public ShipmentView start(String code,String driverEmail){Viaje trip=assignedTrip(code,driverEmail);if(trip.getEstado()!=EstadoViaje.PROGRAMADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Solo puede iniciarse un viaje programado");LocalDateTime now=LocalDateTime.now();trip.iniciar(now);trip.getCamion().iniciarRuta();trip.getEnvio().actualizarEstado(EstadoEnvio.EN_TRANSITO);Viaje saved=viajes.save(trip);camiones.save(saved.getCamion());envios.save(saved.getEnvio());eventos.save(new EventoViaje(saved,"VIAJE_INICIADO","Inicio registrado por el conductor",now));ShipmentView view=ShipmentMapper.toView(saved);realtime.shipmentChanged("TRIP_STARTED",view);log.info("Trip {} started by {}",code,driverEmail);return view;}
    @Transactional(readOnly=true) public List<ShipmentView> assigned(String email){return viajes.findByConductorUsuarioCorreoIgnoreCaseOrderByIdViajeDesc(email).stream().map(ShipmentMapper::toView).toList();}
    @Transactional(readOnly=true) Viaje assignedTrip(String code,String email){return viajes.findFirstByEnvioIdEnvioAndConductorUsuarioCorreoIgnoreCase(ShipmentCodes.idFromTrackingCode(code),email).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Viaje no encontrado"));}
}