package pe.andeslink.andestrack.shipment;
import pe.andeslink.andestrack.domain.entity.DomainEnums.TipoIncidencia;
import jakarta.validation.constraints.*;
public record IncidentRequest(@NotNull TipoIncidencia type,@NotBlank @Size(max=200) String description){}