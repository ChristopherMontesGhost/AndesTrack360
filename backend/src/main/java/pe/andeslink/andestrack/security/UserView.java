package pe.andeslink.andestrack.security;
import pe.andeslink.andestrack.domain.entity.Usuario;
import pe.andeslink.andestrack.domain.entity.DomainEnums.Rol;
public record UserView(Integer id,String email,String displayName,Rol role){public static UserView from(Usuario u){return new UserView(u.getIdUsuario(),u.getCorreo(),u.getNombre(),u.getRol());}}