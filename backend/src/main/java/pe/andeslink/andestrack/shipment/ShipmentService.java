package pe.andeslink.andestrack.shipment;

import com.google.common.base.Preconditions;
import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.*;
import pe.andeslink.andestrack.domain.repository.*;
import pe.andeslink.andestrack.realtime.RealtimePublisher;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

/** Coordinates the scheduling use case without exposing persistence details to MVC. */
@Service
public class ShipmentService {
    private static final Logger log=LoggerFactory.getLogger(ShipmentService.class);
    private final EnvioRepository envios;private final ViajeRepository viajes;private final ClienteRepository clientes;private final ConductorRepository conductores;private final CamionRepository camiones;private final RutaRepository rutas;private final EventoViajeRepository eventos;private final UsuarioRepository usuarios;private final RouteEstimator estimator;private final RealtimePublisher realtime;
    public ShipmentService(EnvioRepository envios,ViajeRepository viajes,ClienteRepository clientes,ConductorRepository conductores,CamionRepository camiones,RutaRepository rutas,EventoViajeRepository eventos,UsuarioRepository usuarios,RouteEstimator estimator,RealtimePublisher realtime){this.envios=envios;this.viajes=viajes;this.clientes=clientes;this.conductores=conductores;this.camiones=camiones;this.rutas=rutas;this.eventos=eventos;this.usuarios=usuarios;this.estimator=estimator;this.realtime=realtime;}
    @Transactional public ShipmentView create(CreateShipmentRequest r,String programadorEmail){
        Cliente cliente=clientes.findByUsuarioCorreoIgnoreCase(r.clientEmail()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente no registrado"));
        Conductor conductor=conductores.findByUsuarioCorreoIgnoreCase(r.driverEmail()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Conductor no registrado"));
        Camion camion=camiones.findByPlacaIgnoreCase(r.truckPlate()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Camión no registrado"));
        Usuario programador=usuarios.findByCorreoIgnoreCase(programadorEmail).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        Preconditions.checkArgument(camion.getEstado()==EstadoCamion.DISPONIBLE,"El camión no está disponible");
        Preconditions.checkArgument(r.weightTon().compareTo(camion.getCapacidadTon())<=0,"La carga excede la capacidad del camión");
        BigDecimal km=estimator.distanceKm(r.origin(),r.destination());
        Ruta ruta=rutas.findFirstByOrigenIgnoreCaseAndDestinoIgnoreCase(r.origin(),r.destination()).orElseGet(()->rutas.save(new Ruta(r.origin(),r.destination(),km)));
        var departure=r.scheduledDeparture();var eta=estimator.eta(ruta.getDistanciaKm(),r.averageSpeedKmh(),departure);
        Envio envio=envios.save(new Envio(cliente,ruta,programador,r.cargo().trim(),r.weightTon(),r.declaredValueUsd()));
        Viaje viaje=viajes.save(new Viaje(envio,camion,conductor,departure,r.averageSpeedKmh(),eta));camion.asignar();camiones.save(camion);
        eventos.save(new EventoViaje(viaje,"ENVIO_PROGRAMADO","Envío programado por "+programador.getNombre(),departure));
        ShipmentView view=ShipmentMapper.toView(viaje);realtime.shipmentChanged("SHIPMENT_CREATED",view);log.info("Shipment {} scheduled for {}",view.trackingCode(),view.destination());return view;
    }
    @Transactional(readOnly=true) public List<ShipmentView> all(){return viajes.findAllByOrderByIdViajeDesc().stream().map(ShipmentMapper::toView).toList();}
    @Transactional(readOnly=true) public List<ShipmentView> assignedTo(String email){return viajes.findByConductorUsuarioCorreoIgnoreCaseOrderByIdViajeDesc(email).stream().map(ShipmentMapper::toView).toList();}
    @Transactional(readOnly=true) public List<ShipmentView> forClient(String email){return viajes.findByEnvioClienteUsuarioCorreoIgnoreCaseOrderByIdViajeDesc(email).stream().map(ShipmentMapper::toView).toList();}
    @Transactional(readOnly=true) public ShipmentView forClient(String code,String email){Viaje trip=byCode(code);if(!trip.getEnvio().getCliente().getUsuario().getCorreo().equalsIgnoreCase(email))throw new ResponseStatusException(HttpStatus.NOT_FOUND);return ShipmentMapper.toView(trip);}
    @Transactional(readOnly=true) public Viaje byCode(String code){return viajes.findFirstByEnvioIdEnvioOrderByIdViajeDesc(ShipmentCodes.idFromTrackingCode(code)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Envío no encontrado"));}
}