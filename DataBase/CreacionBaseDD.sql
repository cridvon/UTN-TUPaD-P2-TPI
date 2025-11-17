-- Crear base de datos y tablas para el TPI Programación 2
DROP DATABASE IF EXISTS tpip2;
CREATE DATABASE tpip2
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE tpip2;

-- Tabla de domicilios fiscales (B)
CREATE TABLE Domicilios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT NULL
) ENGINE=InnoDB;

-- Tabla de empresas (A)
CREATE TABLE Empresas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    razonSocial VARCHAR(120) NOT NULL,
    cuit VARCHAR(13) NOT NULL,
    actividadPrincipal VARCHAR(80),
    email VARCHAR(120),
    domicilioFiscal_id BIGINT UNIQUE, -- Relación 1 a 1 con Domicilios.id

    CONSTRAINT uq_empresas_cuit UNIQUE (cuit),

    CONSTRAINT fk_empresa_domicilio
        FOREIGN KEY (domicilioFiscal_id)
        REFERENCES Domicilios(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB;
