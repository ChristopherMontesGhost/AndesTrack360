package pe.andeslink.andestrack.security;

import pe.andeslink.andestrack.domain.entity.*;
import pe.andeslink.andestrack.domain.entity.DomainEnums.*;
import pe.andeslink.andestrack.domain.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.Locale;

/** Opt-in local demo accounts; no demo credentials are provisioned in production by default. */
@Configuration
public class BootstrapDemoData {
    @Bean ApplicationRunner seedDemoAccounts(UsuarioRepository users,ConductorRepository drivers,ClienteRepository clients,CamionRepository trucks,PasswordEncoder encoder,@Value("${app.demo-data.enabled:false}") boolean enabled,@Value("${app.demo-data.password:}") String password){
        return args->{if(!enabled||password==null||password.isBlank())return;ensureUser(users,encoder,"Diego Quispe","conductor@andeslink.pe",Rol.DRIVER,password);ensureUser(users,encoder,"Camila Torres","coordinador@andeslink.pe",Rol.COORDINATOR,password);Usuario client=ensureUser(users,encoder,"María Paredes","cliente@andeslink.pe",Rol.CLIENT,password);if(clients.findByUsuarioCorreoIgnoreCase(client.getCorreo()).isEmpty())clients.save(new Cliente(client,"Cliente AndesLink Demo","999999999"));Usuario driver=users.findByCorreoIgnoreCase("conductor@andeslink.pe").orElseThrow();if(drivers.findByUsuarioCorreoIgnoreCase(driver.getCorreo()).isEmpty())drivers.save(new Conductor(driver,"A12345678"));seedTruck(trucks,"ATL-001","Volvo FH",25.0);seedTruck(trucks,"ATL-002","Scania R450",28.0);};
    }
    private Usuario ensureUser(UsuarioRepository users,PasswordEncoder encoder,String name,String email,Rol role,String password){return users.findByCorreoIgnoreCase(email).orElseGet(()->users.save(new Usuario(name,email.toLowerCase(Locale.ROOT),encoder.encode(password),role)));}
    private void seedTruck(CamionRepository trucks,String plate,String model,double capacity){if(trucks.findByPlacaIgnoreCase(plate).isEmpty())trucks.save(new Camion(plate,model,BigDecimal.valueOf(capacity),EstadoCamion.DISPONIBLE));}
}