package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name="conductor")
public class Conductor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_conductor") private Integer idConductor;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_usuario",nullable=false,unique=true) private Usuario usuario;
    @Column(nullable=false,length=15) private String licencia;
    @Column(nullable=false) private int puntos=0;
    @Column(name="ranking_semanal",nullable=false) private int rankingSemanal=0;
    @OneToMany(mappedBy="conductor") private Set<Viaje> viajes;
    protected Conductor() {}
    public Conductor(Usuario usuario,String licencia){this.usuario=usuario;this.licencia=licencia;}
    public Integer getIdConductor(){return idConductor;} public Usuario getUsuario(){return usuario;} public String getLicencia(){return licencia;}
}