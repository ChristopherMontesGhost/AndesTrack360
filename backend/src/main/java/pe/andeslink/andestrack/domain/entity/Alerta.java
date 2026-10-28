package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="alerta")
public class Alerta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_alerta") private Integer idAlerta;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_incidencia",nullable=false,unique=true) private Incidencia incidencia;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_atendida_por") private Usuario atendidaPor;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private DomainEnums.SeveridadAlerta severidad;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private DomainEnums.EstadoAlerta estado;
    @Column(name="fecha_hora",nullable=false) private LocalDateTime fechaHora;
    protected Alerta() {}
    public Alerta(Incidencia incidencia,DomainEnums.SeveridadAlerta severidad,LocalDateTime fechaHora){this.incidencia=incidencia;this.severidad=severidad;this.fechaHora=fechaHora;this.estado=DomainEnums.EstadoAlerta.ACTIVA;}
    public Integer getIdAlerta(){return idAlerta;} public Incidencia getIncidencia(){return incidencia;} public Usuario getAtendidaPor(){return atendidaPor;} public DomainEnums.SeveridadAlerta getSeveridad(){return severidad;} public DomainEnums.EstadoAlerta getEstado(){return estado;} public LocalDateTime getFechaHora(){return fechaHora;}
    public void atender(Usuario usuario,DomainEnums.EstadoAlerta estado){if(estado==DomainEnums.EstadoAlerta.ACTIVA)throw new IllegalArgumentException("La alerta debe cerrarse en un estado final");this.atendidaPor=usuario;this.estado=estado;this.incidencia.actualizarEstado(estado==DomainEnums.EstadoAlerta.DESCARTADA?DomainEnums.EstadoIncidencia.DESCARTADA:DomainEnums.EstadoIncidencia.RESUELTA);}
}