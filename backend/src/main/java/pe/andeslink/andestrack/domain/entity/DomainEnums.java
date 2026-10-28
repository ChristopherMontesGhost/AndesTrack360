package pe.andeslink.andestrack.domain.entity;

/** Catalogs stored in the ENUM columns defined by the AndesTrack schema. */
public final class DomainEnums {
    private DomainEnums() {}
    public enum Rol { ADMIN, DRIVER, COORDINATOR, CLIENT }
    public enum EstadoEnvio { PROGRAMADO, EN_TRANSITO, ENTREGADO, DEMORADO, CANCELADO }
    public enum EstadoViaje { PROGRAMADO, EN_CURSO, DEMORADO, FINALIZADO, CANCELADO }
    public enum EstadoCamion { DISPONIBLE, ASIGNADO, EN_RUTA, MANTENIMIENTO, INACTIVO }
    public enum TipoIncidencia { TRAFICO_INTENSO, ACCIDENTE, FALLA_MECANICA, CLIMA_ADVERSO, BLOQUEO, OTRO }
    public enum EstadoIncidencia { PENDIENTE, EN_GESTION, RESUELTA, DESCARTADA }
    public enum SeveridadAlerta { LOW, MEDIUM, HIGH, CRITICAL }
    public enum EstadoAlerta { ACTIVA, REVISADA, DESCARTADA }
}