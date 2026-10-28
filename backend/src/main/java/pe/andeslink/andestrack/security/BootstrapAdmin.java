package pe.andeslink.andestrack.security;
import pe.andeslink.andestrack.domain.entity.Usuario;
import pe.andeslink.andestrack.domain.entity.DomainEnums.Rol;
import pe.andeslink.andestrack.domain.repository.UsuarioRepository;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class BootstrapAdmin {
    private static final Logger log=LoggerFactory.getLogger(BootstrapAdmin.class);
    @Bean ApplicationRunner createInitialAdmin(UsuarioRepository users,PasswordEncoder encoder,@Value("${app.bootstrap-admin.email}") String email,@Value("${app.bootstrap-admin.password}") String password){
        return args->{if(password==null||password.isBlank())return;if(users.findByCorreoIgnoreCase(email).isEmpty()){users.save(new Usuario("Administrador AndesLink",email.toLowerCase(),encoder.encode(password),Rol.ADMIN));log.info("Bootstrap administrator provisioned: {}",email);}};
    }
}