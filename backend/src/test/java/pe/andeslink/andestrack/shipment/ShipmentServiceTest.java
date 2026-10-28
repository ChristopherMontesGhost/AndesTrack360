package pe.andeslink.andestrack.shipment;

import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.*;
import pe.andeslink.andestrack.domain.repository.*;
import pe.andeslink.andestrack.realtime.RealtimePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
class ShipmentServiceTest {
    EnvioRepository envios;ViajeRepository viajes;ClienteRepository clientes;ConductorRepository conductores;CamionRepository camiones;RutaRepository rutas;EventoViajeRepository eventos;UsuarioRepository usuarios;RealtimePublisher realtime;ShipmentService service;
    @BeforeEach void setUp(){envios=mock(EnvioRepository.class);viajes=mock(ViajeRepository.class);clientes=mock(ClienteRepository.class);conductores=mock(ConductorRepository.class);camiones=mock(CamionRepository.class);rutas=mock(RutaRepository.class);eventos=mock(EventoViajeRepository.class);usuarios=mock(UsuarioRepository.class);realtime=mock(RealtimePublisher.class);service=new ShipmentService(envios,viajes,clientes,conductores,camiones,rutas,eventos,usuarios,new RouteEstimator(),realtime);}
    @Test void schedulesShipmentAndCreatesLinkedTripAndEvent(){String clientEmail="cliente@andeslink.pe",driverEmail="conductor@andeslink.pe",adminEmail="admin@andeslink.pe";Usuario clientUser=new Usuario("María Paredes",clientEmail,"hash",Rol.CLIENT),driverUser=new Usuario("Diego Quispe",driverEmail,"hash",Rol.DRIVER),admin=new Usuario("Valeria Rojas",adminEmail,"hash",Rol.ADMIN);Cliente client=new Cliente(clientUser,"AndesLink Demo","999999999");Conductor driver=new Conductor(driverUser,"A12345678");Camion truck=new Camion("ATL-001","Volvo FH",new BigDecimal("25.0"),EstadoCamion.DISPONIBLE);when(clientes.findByUsuarioCorreoIgnoreCase(clientEmail)).thenReturn(Optional.of(client));when(conductores.findByUsuarioCorreoIgnoreCase(driverEmail)).thenReturn(Optional.of(driver));when(camiones.findByPlacaIgnoreCase("ATL-001")).thenReturn(Optional.of(truck));when(usuarios.findByCorreoIgnoreCase(adminEmail)).thenReturn(Optional.of(admin));when(rutas.findFirstByOrigenIgnoreCaseAndDestinoIgnoreCase("Lima","Arequipa")).thenReturn(Optional.empty());when(rutas.save(any(Ruta.class))).thenAnswer(i->i.getArgument(0));when(envios.save(any(Envio.class))).thenAnswer(i->{Envio value=i.getArgument(0);ReflectionTestUtils.setField(value,"idEnvio",77);return value;});when(viajes.save(any(Viaje.class))).thenAnswer(i->i.getArgument(0));CreateShipmentRequest request=new CreateShipmentRequest(clientEmail,driverEmail,"ATL-001","Equipos médicos",new BigDecimal("1.00"),new BigDecimal("500.00"),"Lima","Arequipa",LocalDateTime.of(2026,10,28,8,0),new BigDecimal("55.0"));ShipmentView result=service.create(request,adminEmail);assertEquals("AT-0077",result.trackingCode());assertEquals("PLANNED",result.status());assertTrue(result.distanceKm()>700);assertEquals(EstadoCamion.ASIGNADO,truck.getEstado());verify(envios).save(any(Envio.class));verify(viajes).save(any(Viaje.class));verify(eventos).save(any(EventoViaje.class));verify(realtime).shipmentChanged(eq("SHIPMENT_CREATED"),eq(result));}
}