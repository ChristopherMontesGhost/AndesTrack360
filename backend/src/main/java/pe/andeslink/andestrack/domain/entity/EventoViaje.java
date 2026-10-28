package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="evento_viaje")
public class EventoViaje {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_evento") private Integer idEvento;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_viaje",nullable=false) private Viaje viaje;
    @Column(nullable=false,length=40) private String tipo;
    @Column(nullable=false,length=200) private String detalle;
    @Column(name="fecha_hora",nullable=false) private LocalDateTime fechaHora;
    protected EventoViaje() {}
    public EventoViaje(Viaje viaje,String tipo,String detalle,LocalDateTime fechaHora){this.viaje=viaje;this.tipo=tipo;this.detalle=detalle;this.fechaHora=fechaHora;}
    public Integer getIdEvento(){return idEvento;} public Viaje getViaje(){return viaje;} public String getTipo(){return tipo;} public String getDetalle(){return detalle;} public LocalDateTime getFechaHora(){return fechaHora;}
}