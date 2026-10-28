package pe.andeslink.andestrack.security;

import pe.andeslink.andestrack.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarios;
    public UserDetailsServiceImpl(UsuarioRepository usuarios){this.usuarios=usuarios;}
    @Override public UserDetails loadUserByUsername(String correo)throws UsernameNotFoundException{return usuarios.findByCorreoIgnoreCase(correo).orElseThrow(()->new UsernameNotFoundException("Cuenta no encontrada"));}
}