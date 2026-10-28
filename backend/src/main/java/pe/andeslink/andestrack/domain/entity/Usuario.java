package pe.andeslink.andestrack.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/** Cuenta de acceso del sistema y entidad raíz para roles operativos. */
@Entity @Table(name="usuario")
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_usuario") private Integer idUsuario;
    @Column(nullable=false,length=100) private String nombre;
    @Column(nullable=false,unique=true,length=120) private String correo;
    @Column(name="password_hash",nullable=false,length=60) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=16) private DomainEnums.Rol rol;
    @Column(nullable=false) private boolean activo=true;
    @OneToOne(mappedBy="usuario",fetch=FetchType.LAZY) private Conductor conductor;
    @OneToOne(mappedBy="usuario",fetch=FetchType.LAZY) private Cliente cliente;
    @OneToMany(mappedBy="programador") private Set<Envio> enviosProgramados;
    protected Usuario() {}
    public Usuario(String nombre,String correo,String passwordHash,DomainEnums.Rol rol){this.nombre=nombre;this.correo=correo;this.passwordHash=passwordHash;this.rol=rol;this.activo=true;}
    public Integer getIdUsuario(){return idUsuario;} public String getNombre(){return nombre;} public String getCorreo(){return correo;} public DomainEnums.Rol getRol(){return rol;}
    public void desactivar(){this.activo=false;}
    @Override public Collection<? extends GrantedAuthority> getAuthorities(){return List.of(new SimpleGrantedAuthority("ROLE_"+rol.name()));}
    @Override public String getPassword(){return passwordHash;} @Override public String getUsername(){return correo;}
    @Override public boolean isAccountNonExpired(){return true;} @Override public boolean isAccountNonLocked(){return true;} @Override public boolean isCredentialsNonExpired(){return true;} @Override public boolean isEnabled(){return activo;}
}