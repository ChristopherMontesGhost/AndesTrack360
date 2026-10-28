package pe.andeslink.andestrack.security;
import pe.andeslink.andestrack.domain.entity.DomainEnums.Rol;
import jakarta.validation.constraints.*;
public record CreateUserRequest(@NotBlank @Email @Size(max=120) String email,@NotBlank @Size(max=100) String displayName,@NotBlank @Size(min=10,max=72) String password,@NotNull Rol role,@Size(max=120) String company,@Size(max=20) String phone,@Size(max=15) String license){}