CREATE TABLE user_account (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(190) NOT NULL,
  display_name VARCHAR(120) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(24) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  CONSTRAINT uk_user_account_email UNIQUE (email)
);

CREATE TABLE shipment (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tracking_code VARCHAR(20) NOT NULL,
  client_name VARCHAR(120) NOT NULL,
  client_email VARCHAR(190) NOT NULL,
  driver_name VARCHAR(120) NOT NULL,
  driver_email VARCHAR(190) NOT NULL,
  truck_code VARCHAR(20) NOT NULL,
  cargo VARCHAR(160) NOT NULL,
  origin VARCHAR(80) NOT NULL,
  destination VARCHAR(80) NOT NULL,
  distance_km DOUBLE NOT NULL,
  eta TIMESTAMP(6) NOT NULL,
  status VARCHAR(24) NOT NULL,
  progress_pct INT NOT NULL DEFAULT 0,
  latitude DOUBLE NULL,
  longitude DOUBLE NULL,
  updated_at TIMESTAMP(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_shipment_tracking UNIQUE (tracking_code),
  INDEX idx_shipment_client (client_email),
  INDEX idx_shipment_driver (driver_email),
  INDEX idx_shipment_status (status)
);

CREATE TABLE incident (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tracking_code VARCHAR(20) NULL,
  truck_code VARCHAR(20) NOT NULL,
  incident_type VARCHAR(24) NOT NULL,
  severity VARCHAR(20) NOT NULL,
  description VARCHAR(500) NOT NULL,
  resolved BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP(6) NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_incident_opened (resolved, created_at)
);
