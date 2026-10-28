package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
@Entity @Table(name="camion")
public class Camion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_camion") private Integer idCamion;
    @Column(nullable=false,unique=true,length=10) private String placa;
    @Column(nullable=false,length=60) private String modelo;
    @Column(name="capacidad_ton",nullable=false,precision=5,scale=1) private BigDecimal capacidadTon;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private DomainEnums.EstadoCamion estado;
    @OneToMany(mappedBy="camion") private Set<Viaje> viajes;
    protected Camion() {}
    public Camion(String placa,String modelo,BigDecimal capacidadTon,DomainEnums.EstadoCamion estado){this.placa=placa;this.modelo=modelo;this.capacidadTon=capacidadTon;this.estado=estado;}
    public Integer getIdCamion(){return idCamion;} public String getPlaca(){return placa;} public String getModelo(){return modelo;} public BigDecimal getCapacidadTon(){return capacidadTon;} public DomainEnums.EstadoCamion getEstado(){return estado;}
    public void asignar(){if(estado!=DomainEnums.EstadoCamion.DISPONIBLE)throw new IllegalStateException("El camión no está disponible");estado=DomainEnums.EstadoCamion.ASIGNADO;}
    public void liberar(){if(estado==DomainEnums.EstadoCamion.EN_RUTA)estado=DomainEnums.EstadoCamion.DISPONIBLE;}
    public void iniciarRuta(){if(estado!=DomainEnums.EstadoCamion.ASIGNADO)throw new IllegalStateException("El camión no está asignado");estado=DomainEnums.EstadoCamion.EN_RUTA;}
}