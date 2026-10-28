package pe.andeslink.andestrack.auth;

import pe.andeslink.andestrack.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

/** Authenticates an account and issues a short-lived, role-bearing JWT. */
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long ttlMinutes;
    public AuthService(AuthenticationManager authenticationManager,JwtEncoder jwtEncoder,@Value("${app.jwt.issuer}") String issuer,@Value("${app.jwt.ttl-minutes}") long ttlMinutes){this.authenticationManager=authenticationManager;this.jwtEncoder=jwtEncoder;this.issuer=issuer;this.ttlMinutes=ttlMinutes;}
    public LoginResponse login(LoginRequest request){
        Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(),request.password()));
        Usuario user=(Usuario)auth.getPrincipal(); Instant now=Instant.now(); List<String> roles=List.of(user.getRol().name());
        JwtClaimsSet claims=JwtClaimsSet.builder().issuer(issuer).issuedAt(now).expiresAt(now.plusSeconds(ttlMinutes*60)).subject(user.getCorreo()).claim("roles",roles).build();
        String token=jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
        return new LoginResponse(token,"Bearer",ttlMinutes*60,user.getCorreo(),user.getNombre(),roles);
    }
}