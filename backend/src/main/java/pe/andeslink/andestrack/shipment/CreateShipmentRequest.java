package pe.andeslink.andestrack.shipment;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record CreateShipmentRequest(@NotBlank @Email @Size(max=120) String clientEmail,@NotBlank @Email @Size(max=120) String driverEmail,@NotBlank @Size(max=10) String truckPlate,@NotBlank @Size(max=150) String cargo,@NotNull @DecimalMin("0.01") @Digits(integer=4,fraction=2) BigDecimal weightTon,@NotNull @DecimalMin("0.00") @Digits(integer=10,fraction=2) BigDecimal declaredValueUsd,@NotBlank @Size(max=60) String origin,@NotBlank @Size(max=60) String destination,@NotNull LocalDateTime scheduledDeparture,@NotNull @DecimalMin("1.0") @DecimalMax("130.0") @Digits(integer=3,fraction=1) BigDecimal averageSpeedKmh){}