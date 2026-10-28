package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
@Entity @Table(name="viaje")
public class Viaje {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_viaje") private Integer idViaje;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_envio",nullable=false) private Envio envio;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_camion",nullable=false) private Camion camion;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_conductor",nullable=false) private Conductor conductor;
    @Column(name="salida_programada",nullable=false) private LocalDateTime salidaProgramada;
    @Column(name="inicio_real") private LocalDateTime inicioReal;
    @Column(name="fin_real") private LocalDateTime finReal;
    @Column(name="velocidad_prom",nullable=false,precision=5,scale=1) private BigDecimal velocidadProm;
    @Column(name="eta_estimada",nullable=false) private LocalDateTime etaEstimada;
    @Column(name="progreso_pct",nullable=false) private int progresoPct;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=16) private DomainEnums.EstadoViaje estado;
    @OneToMany(mappedBy="viaje") private Set<EventoViaje> eventos;
    @OneToMany(mappedBy="viaje") private Set<Incidencia> incidencias;
    protected Viaje() {}
    public Viaje(Envio envio,Camion camion,Conductor conductor,LocalDateTime salidaProgramada,BigDecimal velocidadProm,LocalDateTime etaEstimada){this.envio=envio;this.camion=camion;this.conductor=conductor;this.salidaProgramada=salidaProgramada;this.velocidadProm=velocidadProm;this.etaEstimada=etaEstimada;this.progresoPct=0;this.estado=DomainEnums.EstadoViaje.PROGRAMADO;}
    public Integer getIdViaje(){return idViaje;} public Envio getEnvio(){return envio;} public Camion getCamion(){return camion;} public Conductor getConductor(){return conductor;} public LocalDateTime getSalidaProgramada(){return salidaProgramada;} public LocalDateTime getInicioReal(){return inicioReal;} public LocalDateTime getFinReal(){return finReal;} public BigDecimal getVelocidadProm(){return velocidadProm;} public LocalDateTime getEtaEstimada(){return etaEstimada;} public int getProgresoPct(){return progresoPct;} public DomainEnums.EstadoViaje getEstado(){return estado;}
    public void iniciar(LocalDateTime ahora){if(estado!=DomainEnums.EstadoViaje.PROGRAMADO)throw new IllegalStateException("Solo puede iniciarse un viaje programado");inicioReal=ahora;progresoPct=Math.max(1,progresoPct);estado=DomainEnums.EstadoViaje.EN_CURSO;}
    public void demorar(){if(estado==DomainEnums.EstadoViaje.EN_CURSO)estado=DomainEnums.EstadoViaje.DEMORADO;}
}