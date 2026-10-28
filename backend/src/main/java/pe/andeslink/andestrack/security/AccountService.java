package pe.andeslink.andestrack.security;
import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.Rol;
import pe.andeslink.andestrack.domain.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Locale;
/** Creates a Usuario and its one-to-one profile for client or driver roles. */
@Service
public class AccountService {
    private final UsuarioRepository users; private final ClienteRepository clients; private final ConductorRepository drivers; private final PasswordEncoder passwords;
    public AccountService(UsuarioRepository users,ClienteRepository clients,ConductorRepository drivers,PasswordEncoder passwords){this.users=users;this.clients=clients;this.drivers=drivers;this.passwords=passwords;}
    @Transactional public UserView create(CreateUserRequest request){
        String email=StringUtils.trimToEmpty(request.email()).toLowerCase(Locale.ROOT);
        if(users.findByCorreoIgnoreCase(email).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"El correo ya está registrado");
        Usuario u=users.save(new Usuario(StringUtils.trimToEmpty(request.displayName()),email,passwords.encode(request.password()),request.role()));
        if(request.role()==Rol.DRIVER)drivers.save(new Conductor(u,StringUtils.defaultIfBlank(request.license(),"PENDIENTE")));
        if(request.role()==Rol.CLIENT)clients.save(new Cliente(u,StringUtils.defaultIfBlank(request.company(),request.displayName()),StringUtils.defaultString(request.phone())));
        return UserView.from(u);
    }
}