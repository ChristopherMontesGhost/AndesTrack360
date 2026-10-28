package pe.andeslink.andestrack.fleet;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateTruckRequest(@NotBlank @Size(max=10) String plate,@NotBlank @Size(max=60) String model,@NotNull @DecimalMin("0.1") @Digits(integer=4,fraction=1) BigDecimal capacityTon){}