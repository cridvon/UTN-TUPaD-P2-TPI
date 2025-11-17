USE tpip2;

-- Domicilios de prueba
INSERT INTO Domicilios (eliminado, calle, numero)
VALUES
  (FALSE, 'Av. Córdoba', 742),
  (FALSE, 'San Martín', 1234),
  (FALSE, 'Belgrano', 567),
  (FALSE, 'Oncativo', 1999);

-- Empresas de prueba (algunas con domicilio, otras sin)
INSERT INTO Empresas (eliminado, razonSocial, cuit, actividadPrincipal, email, domicilioFiscal_id)
VALUES
  (FALSE, 'El trébol S.A.',  '30-12345678-9', 'Fabricación de explosivos', 'contacto@acme.com',         1),
  (FALSE, 'Tech Solutions SRL',  '30-87654321-0', 'Servicios de software',     'info@techsolutions.com',    2),
  (FALSE, 'Transporte Norte S.A.','30-11112222-3','Transporte de cargas',      'contacto@tnorte.com',       NULL),
  (FALSE, 'Panificados del Sur','30-99998888-7', 'Panadería industrial',      'ventas@panificados.com',    3);
