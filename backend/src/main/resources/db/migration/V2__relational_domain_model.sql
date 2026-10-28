-- AndesTrack 360 relational model used by the integrated flows.
-- The earlier flattened V1 tables are retained for safe migration; application code no longer writes to them.
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(120) NOT NULL,
  password_hash CHAR(60) NOT NULL,
  rol ENUM('ADMIN','DRIVER','COORDINATOR','CLIENT') NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id_usuario),
  CONSTRAINT uk_usuario_correo UNIQUE (correo)
);

CREATE TABLE conductor (
  id_conductor INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  licencia VARCHAR(15) NOT NULL,
  puntos INT NOT NULL DEFAULT 0,
  ranking_semanal INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id_conductor),
  CONSTRAINT uk_conductor_usuario UNIQUE (id_usuario),
  CONSTRAINT fk_conductor_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE cliente (
  id_cliente INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  empresa VARCHAR(120) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  PRIMARY KEY (id_cliente),
  CONSTRAINT uk_cliente_usuario UNIQUE (id_usuario),
  CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE ruta (
  id_ruta INT NOT NULL AUTO_INCREMENT,
  origen VARCHAR(60) NOT NULL,
  destino VARCHAR(60) NOT NULL,
  distancia_km DECIMAL(7,1) NOT NULL,
  PRIMARY KEY (id_ruta)
);

CREATE TABLE camion (
  id_camion INT NOT NULL AUTO_INCREMENT,
  placa VARCHAR(10) NOT NULL,
  modelo VARCHAR(60) NOT NULL,
  capacidad_ton DECIMAL(5,1) NOT NULL,
  estado ENUM('DISPONIBLE','ASIGNADO','EN_RUTA','MANTENIMIENTO','INACTIVO') NOT NULL,
  PRIMARY KEY (id_camion),
  CONSTRAINT uk_camion_placa UNIQUE (placa)
);

CREATE TABLE envio (
  id_envio INT NOT NULL AUTO_INCREMENT,
  id_cliente INT NOT NULL,
  id_ruta INT NOT NULL,
  id_programador INT NOT NULL,
  descripcion VARCHAR(150) NOT NULL,
  peso_ton DECIMAL(6,2) NOT NULL,
  valor_usd DECIMAL(12,2) NOT NULL,
  estado ENUM('PROGRAMADO','EN_TRANSITO','ENTREGADO','DEMORADO','CANCELADO') NOT NULL,
  PRIMARY KEY (id_envio),
  KEY idx_envio_cliente (id_cliente),
  KEY idx_envio_ruta (id_ruta),
  KEY idx_envio_estado (estado),
  CONSTRAINT fk_envio_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  CONSTRAINT fk_envio_ruta FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta),
  CONSTRAINT fk_envio_programador FOREIGN KEY (id_programador) REFERENCES usuario(id_usuario)
);

CREATE TABLE viaje (
  id_viaje INT NOT NULL AUTO_INCREMENT,
  id_envio INT NOT NULL,
  id_camion INT NOT NULL,
  id_conductor INT NOT NULL,
  salida_programada DATETIME NOT NULL,
  inicio_real DATETIME NULL,
  fin_real DATETIME NULL,
  velocidad_prom DECIMAL(5,1) NOT NULL,
  eta_estimada DATETIME NOT NULL,
  progreso_pct TINYINT UNSIGNED NOT NULL DEFAULT 0,
  estado ENUM('PROGRAMADO','EN_CURSO','DEMORADO','FINALIZADO','CANCELADO') NOT NULL,
  PRIMARY KEY (id_viaje),
  KEY idx_viaje_estado (estado),
  CONSTRAINT fk_viaje_envio FOREIGN KEY (id_envio) REFERENCES envio(id_envio),
  CONSTRAINT fk_viaje_camion FOREIGN KEY (id_camion) REFERENCES camion(id_camion),
  CONSTRAINT fk_viaje_conductor FOREIGN KEY (id_conductor) REFERENCES conductor(id_conductor)
);

CREATE TABLE evento_viaje (
  id_evento INT NOT NULL AUTO_INCREMENT,
  id_viaje INT NOT NULL,
  tipo VARCHAR(40) NOT NULL,
  detalle VARCHAR(200) NOT NULL,
  fecha_hora DATETIME NOT NULL,
  PRIMARY KEY (id_evento),
  KEY idx_evento_viaje_fecha (id_viaje, fecha_hora),
  CONSTRAINT fk_evento_viaje FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
);

CREATE TABLE incidencia (
  id_incidencia INT NOT NULL AUTO_INCREMENT,
  id_viaje INT NOT NULL,
  tipo ENUM('TRAFICO_INTENSO','ACCIDENTE','FALLA_MECANICA','CLIMA_ADVERSO','BLOQUEO','OTRO') NOT NULL,
  descripcion VARCHAR(200) NOT NULL,
  fecha_hora DATETIME NOT NULL,
  estado ENUM('PENDIENTE','EN_GESTION','RESUELTA','DESCARTADA') NOT NULL,
  PRIMARY KEY (id_incidencia),
  KEY idx_incidencia_viaje_fecha (id_viaje, fecha_hora),
  CONSTRAINT fk_incidencia_viaje FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
);

CREATE TABLE alerta (
  id_alerta INT NOT NULL AUTO_INCREMENT,
  id_incidencia INT NOT NULL,
  id_atendida_por INT NULL,
  severidad ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL,
  estado ENUM('ACTIVA','REVISADA','DESCARTADA') NOT NULL,
  fecha_hora DATETIME NOT NULL,
  PRIMARY KEY (id_alerta),
  CONSTRAINT uk_alerta_incidencia UNIQUE (id_incidencia),
  KEY idx_alerta_estado_fecha (estado, fecha_hora),
  CONSTRAINT fk_alerta_incidencia FOREIGN KEY (id_incidencia) REFERENCES incidencia(id_incidencia),
  CONSTRAINT fk_alerta_usuario FOREIGN KEY (id_atendida_por) REFERENCES usuario(id_usuario)
);