package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="incidencia")
public class Incidencia {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_incidencia") private Integer idIncidencia;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_viaje",nullable=false) private Viaje viaje;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private DomainEnums.TipoIncidencia tipo;
    @Column(nullable=false,length=200) private String descripcion;
    @Column(name="fecha_hora",nullable=false) private LocalDateTime fechaHora;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=16) private DomainEnums.EstadoIncidencia estado;
    @OneToOne(mappedBy="incidencia",fetch=FetchType.LAZY) private Alerta alerta;
    protected Incidencia() {}
    public Incidencia(Viaje viaje,DomainEnums.TipoIncidencia tipo,String descripcion,LocalDateTime fechaHora){this.viaje=viaje;this.tipo=tipo;this.descripcion=descripcion;this.fechaHora=fechaHora;this.estado=DomainEnums.EstadoIncidencia.PENDIENTE;}
    public Integer getIdIncidencia(){return idIncidencia;} public Viaje getViaje(){return viaje;} public DomainEnums.TipoIncidencia getTipo(){return tipo;} public String getDescripcion(){return descripcion;} public LocalDateTime getFechaHora(){return fechaHora;} public DomainEnums.EstadoIncidencia getEstado(){return estado;} public Alerta getAlerta(){return alerta;}
    public void actualizarEstado(DomainEnums.EstadoIncidencia estado){this.estado=estado;}
}