package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
@Entity @Table(name="ruta")
public class Ruta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_ruta") private Integer idRuta;
    @Column(nullable=false,length=60) private String origen;
    @Column(nullable=false,length=60) private String destino;
    @Column(name="distancia_km",nullable=false,precision=7,scale=1) private BigDecimal distanciaKm;
    @OneToMany(mappedBy="ruta") private Set<Envio> envios;
    protected Ruta() {}
    public Ruta(String origen,String destino,BigDecimal distanciaKm){this.origen=origen;this.destino=destino;this.distanciaKm=distanciaKm;}
    public Integer getIdRuta(){return idRuta;} public String getOrigen(){return origen;} public String getDestino(){return destino;} public BigDecimal getDistanciaKm(){return distanciaKm;}
}