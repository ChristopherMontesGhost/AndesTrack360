# AndesTrack 360° — prototipo integrado

Prototipo de AndesLink Logistics con un frontend navegable para los cuatro roles y una API base en Java/Spring Boot. La demo del navegador funciona con datos locales simulados; el backend ofrece la estructura para reemplazar esa simulación con autenticación JWT, MySQL y eventos STOMP.

## Ejecutar la demo web

1. Abre una terminal en esta carpeta.
2. Sirve el frontend:

       python -m http.server 5500 --directory frontend

3. Abre http://localhost:5500.
4. Usa una de estas cuentas. Todas las contraseñas demo son Andes2026!.

| Rol | Correo |
|---|---|
| Administradora | admin@andeslink.pe |
| Conductor | conductor@andeslink.pe |
| Coordinadora | coordinador@andeslink.pe |
| Cliente | cliente@andeslink.pe |

La sesión simula autenticación y redirige según el rol. AndesAdmin crea envíos y estima distancia/ETA con distancia geodésica multiplicada por un factor vial ilustrativo. AndesDriver inicia viajes y reporta incidencias. AndesControl muestra 120 unidades ficticias, alertas y KPIs. AndesClient sigue el envío AT-2401 de demostración. El estado se guarda en localStorage; BroadcastChannel y el evento storage replican cambios entre pestañas del mismo navegador. El avance simulado corre cada 6.5 segundos.

La pantalla de acceso incluye selección de perfil demo, alternancia de tema, visibilidad de contraseña y una composición animada de marca. El tema oscuro adapta también tablas, alertas, estados, filtros y etiquetas del mapa. El mapa es esquemático; no recibe GPS real. El OTIF es un KPI de muestra. Tailwind se carga desde su CDN de Play para esta demo; para producción, compílalo como CSS local con Vite/Tailwind y fija las versiones.

## Backend Spring Boot + MySQL

Versión fijada: Spring Boot 3.5.16 y Java 21. La línea 3.5 documenta Java 17 como mínimo; Java 21 se eligió como versión LTS. [Requisitos oficiales de Spring Boot 3.5](https://docs.spring.io/spring-boot/3.5/system-requirements.html).

### Arranque local

Desde AndesTrack360/backend prepara las credenciales locales para MySQL. No subas el archivo `.env`:

    Copy-Item .env.example .env

Edita `.env` y reemplaza ambos valores por contraseñas aleatorias. Luego inicia MySQL:

    docker compose up -d

Configura una contraseña para el administrador bootstrap y un secreto aleatorio para JWT. La contraseña frontend demo no se reutiliza en el backend.

    $env:BOOTSTRAP_ADMIN_PASSWORD = 'CambiaEstaClave123!'
    $env:JWT_SECRET_BASE64 = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
    $env:DB_PASSWORD = 'La misma contraseña configurada en backend/.env'
    mvn spring-boot:run

El backend queda en http://localhost:8080; Flyway crea el esquema al iniciar. El bootstrap crea admin@andeslink.pe solamente si BOOTSTRAP_ADMIN_PASSWORD está definido y la cuenta aún no existe. Usa /api/admin/users para crear cuentas con roles ADMIN, DRIVER, COORDINATOR o CLIENT.

### Estructura entregada

    AndesTrack360/
    ├── frontend/
    │   ├── index.html          # shell y Tailwind CDN
    │   ├── styles.css          # tokens, dark mode, mapa, microanimaciones
    │   └── app.js              # login demo, módulos, estado simulado
    ├── backend/
    │   ├── pom.xml
    │   ├── docker-compose.yml
    │   └── src/main/
    │       ├── java/pe/andeslink/andestrack/
    │       │   ├── auth/        # login y emisión de JWT
    │       │   ├── security/    # usuarios, roles, filtros JWT, bootstrap
    │       │   ├── shipment/    # REST, casos de uso, entidades y repositorios
    │       │   └── realtime/    # configuración STOMP y eventos
    │       └── resources/
    │           ├── application.yml
    │           └── db/migration/V1__baseline.sql
    └── README.md

La estructura actual agrupa por funcionalidad vertical. Para crecer, conserva los límites entre interfaces (REST/STOMP), aplicación (casos de uso), dominio (reglas y entidades) e infraestructura (JPA/MySQL, broker, proveedores de geocodificación). Evita que controladores calculen reglas o accedan directamente a repositorios.

### REST disponible

| Método | Ruta | Rol | Uso |
|---|---|---|---|
| POST | /api/auth/login | Público | valida credenciales y devuelve JWT con rol |
| GET, POST | /api/admin/shipments | ADMIN | lista o programa envíos |
| POST | /api/admin/users | ADMIN | crea una cuenta con contraseña BCrypt |
| GET | /api/control/shipments | ADMIN, COORDINATOR | consulta la flota de envíos |
| GET | /api/control/incidents | ADMIN, COORDINATOR | lista alertas activas |
| PATCH | /api/control/incidents/{id}/resolve | ADMIN, COORDINATOR | resuelve una alerta |
| GET | /api/driver/shipments | DRIVER | consulta rutas asignadas |
| POST | /api/driver/shipments/{code}/start | DRIVER | inicia su envío asignado |
| POST | /api/driver/shipments/{code}/incidents | DRIVER | reporta tráfico, clima, falla u otro |
| GET | /api/client/tracking/{code} | CLIENT | consulta su envío; el correo del JWT debe coincidir |

Login de ejemplo:

    { "email": "admin@andeslink.pe", "password": "<clave bootstrap>" }

Usa el token recibido como Authorization: Bearer <JWT> en las rutas protegidas. La autorización se aplica en Spring Security en el servidor; ocultar botones en el frontend nunca sustituye RBAC.

### WebSocket y persistencia

El endpoint STOMP es /ws. El cliente manda el JWT en el header nativo Authorization: Bearer <JWT> del frame CONNECT. Las suscripciones permitidas son /topic/control/shipments y /topic/control/alerts para ADMIN/COORDINATOR, /user/queue/driver para DRIVER y /user/queue/tracking para CLIENT. Los eventos incluyen tipo, envío/alerta y marca de tiempo. [Referencia oficial de Spring STOMP](https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html).

MySQL persiste cuentas, envíos e incidencias mediante JPA y migraciones Flyway. Para alta disponibilidad añade control de concurrencia, auditoría, outbox transaccional y un broker dedicado (por ejemplo RabbitMQ); la configuración integrada del broker es de una sola instancia. Sustituye el cálculo aproximado de rutas por un proveedor de mapas/rutas y alimenta latitud/longitud desde telemetría validada.

### Próximos componentes de producción

- Entidades y gestión de vehículos, mantenimiento, asignaciones y permisos por organización.
- Ingesta de telemetría con control de frecuencia, timestamps y detección de datos obsoletos.
- Motor de rutas con tráfico, zonas horarias y reglas de ETA; el estimador actual solo ilustra el flujo.
- Auditoría, rotación/revocación de tokens, rate limiting, gestión de secretos y política de retención.
- Cliente frontend que cambie de localStorage a REST/STOMP al activar el entorno API.

La demo frontend no envía tráfico a la API todavía: sus cuentas, flota y cambios son de prueba en el navegador. El backend es una base separada lista para integrar, con la protección de cliente por correo ya implementada.
