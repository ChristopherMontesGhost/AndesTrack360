package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import java.util.Set;
@Entity @Table(name="cliente")
public class Cliente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_cliente") private Integer idCliente;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_usuario",nullable=false,unique=true) private Usuario usuario;
    @Column(nullable=false,length=120) private String empresa;
    @Column(nullable=false,length=20) private String telefono;
    @OneToMany(mappedBy="cliente") private Set<Envio> envios;
    protected Cliente() {}
    public Cliente(Usuario usuario,String empresa,String telefono){this.usuario=usuario;this.empresa=empresa;this.telefono=telefono;}
    public Integer getIdCliente(){return idCliente;} public Usuario getUsuario(){return usuario;} public String getEmpresa(){return empresa;}
}