package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
@Entity @Table(name="envio")
public class Envio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_envio") private Integer idEnvio;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_cliente",nullable=false) private Cliente cliente;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_ruta",nullable=false) private Ruta ruta;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="id_programador",nullable=false) private Usuario programador;
    @Column(nullable=false,length=150) private String descripcion;
    @Column(name="peso_ton",nullable=false,precision=6,scale=2) private BigDecimal pesoTon;
    @Column(name="valor_usd",nullable=false,precision=12,scale=2) private BigDecimal valorUsd;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private DomainEnums.EstadoEnvio estado;
    @OneToMany(mappedBy="envio") private Set<Viaje> viajes;
    protected Envio() {}
    public Envio(Cliente cliente,Ruta ruta,Usuario programador,String descripcion,BigDecimal pesoTon,BigDecimal valorUsd){this.cliente=cliente;this.ruta=ruta;this.programador=programador;this.descripcion=descripcion;this.pesoTon=pesoTon;this.valorUsd=valorUsd;this.estado=DomainEnums.EstadoEnvio.PROGRAMADO;}
    public Integer getIdEnvio(){return idEnvio;} public Cliente getCliente(){return cliente;} public Ruta getRuta(){return ruta;} public Usuario getProgramador(){return programador;} public String getDescripcion(){return descripcion;} public BigDecimal getPesoTon(){return pesoTon;} public BigDecimal getValorUsd(){return valorUsd;} public DomainEnums.EstadoEnvio getEstado(){return estado;}
    public void actualizarEstado(DomainEnums.EstadoEnvio estado){this.estado=estado;}
}