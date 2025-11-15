/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Mario Campana
 */


import entities.DomicilioFiscal;
import entities.Empresa;
import service.DomicilioFiscalService;
import service.DomicilioFiscalServiceImpl;
import service.EmpresaService;
import service.EmpresaServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú principal por consola.
 * Conecta la capa de presentación con los servicios.
 */
public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final EmpresaService empresaService = new EmpresaServiceImpl();
    private final DomicilioFiscalService domicilioService = new DomicilioFiscalServiceImpl();

    public void run() {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    ejecutarConManejoErrores(this::crearEmpresa);
                    break;
                case 2:
                    ejecutarConManejoErrores(this::listarEmpresas);
                    break;
                case 3:
                    ejecutarConManejoErrores(this::buscarEmpresaPorCuit);
                    break;
                case 4:
                    ejecutarConManejoErrores(this::actualizarEmpresa);
                    break;
                case 5:
                    ejecutarConManejoErrores(this::eliminarEmpresa);
                    break;
                case 6:
                    ejecutarConManejoErrores(this::crearDomicilio);
                    break;
                case 7:
                    ejecutarConManejoErrores(this::listarDomicilios);
                    break;
                case 8:
                    ejecutarConManejoErrores(this::actualizarDomicilio);
                    break;
                case 9:
                    ejecutarConManejoErrores(this::eliminarDomicilio);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
            System.out.println(); // línea en blanco
        }

        scanner.close();
    }

    // ================= MENÚ =================

    private void mostrarMenu() {
        System.out.println("========= MENU EMPRESAS =========");
        System.out.println("1. Crear empresa (con o sin domicilio)");
        System.out.println("2. Listar empresas");
        System.out.println("3. Buscar empresa por CUIT");
        System.out.println("4. Actualizar empresa");
        System.out.println("5. Eliminar empresa (baja lógica)");
        System.out.println("------------ DOMICILIOS ----------");
        System.out.println("6. Crear domicilio fiscal");
        System.out.println("7. Listar domicilios fiscales");
        System.out.println("8. Actualizar domicilio fiscal");
        System.out.println("9. Eliminar domicilio fiscal (baja lógica)");
        System.out.println("0. Salir");
        System.out.println("=================================");
    }

    // ================= OPCIONES EMPRESA =================

    private void crearEmpresa() {
        System.out.println("== Crear nueva empresa ==");

        String razonSocial = leerTextoObligatorio("Razón social: ");
        String cuit = leerTextoObligatorio("CUIT: ");
        String actividad = leerTextoOpcional("Actividad principal (opcional): ");
        String email = leerTextoOpcional("Email (opcional): ");

        System.out.print("¿Desea crear un domicilio fiscal ahora? (S/N): ");
        String resp = scanner.nextLine().trim().toUpperCase();

        DomicilioFiscal domicilio = null;
        if (resp.startsWith("S")) {
            domicilio = leerDatosDomicilio();
        }

        Empresa empresa = new Empresa(
                null,
                false,
                razonSocial,
                cuit,
                actividad,
                email,
                null
        );

        Empresa creada;
        if (domicilio != null) {
            // Operación transaccional: crea domicilio + empresa en la misma transacción
            creada = empresaService.crearEmpresaConDomicilio(empresa, domicilio);
        } else {
            // Crea solo la empresa
            creada = empresaService.insertar(empresa);
        }

        System.out.println("Empresa creada exitosamente:");
        System.out.println(creada);
    }

    private void listarEmpresas() {
        System.out.println("== Listado de empresas ==");

        List<Empresa> empresas = empresaService.getAll();
        if (empresas.isEmpty()) {
            System.out.println("No hay empresas para mostrar.");
            return;
        }

        for (Empresa e : empresas) {
            System.out.println(e);
        }
    }

    private void buscarEmpresaPorCuit() {
        System.out.println("== Buscar empresa por CUIT ==");

        String cuit = leerTextoObligatorio("Ingrese CUIT a buscar: ");

        Optional<Empresa> opt = empresaService.buscarPorCuit(cuit);
        if (opt.isPresent()) {
            System.out.println("Empresa encontrada:");
            System.out.println(opt.get());
        } else {
            System.out.println("No se encontró ninguna empresa con ese CUIT.");
        }
    }

    private void actualizarEmpresa() {
        System.out.println("== Actualizar empresa ==");

        long id = leerLong("Ingrese ID de la empresa a actualizar: ");

        Optional<Empresa> opt = empresaService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("No existe una empresa con ese ID.");
            return;
        }

        Empresa empresa = opt.get();
        System.out.println("Empresa actual:");
        System.out.println(empresa);

        System.out.println("Deje vacío el campo para mantener el valor actual.");

        String nuevaRazon = leerTextoOpcional("Nueva razón social (" + empresa.getRazonSocial() + "): ");
        if (!nuevaRazon.isBlank()) {
            empresa.setRazonSocial(nuevaRazon);
        }

        String nuevoCuit = leerTextoOpcional("Nuevo CUIT (" + empresa.getCuit() + "): ");
        if (!nuevoCuit.isBlank()) {
            empresa.setCuit(nuevoCuit);
        }

        String nuevaActividad = leerTextoOpcional("Nueva actividad (" + valorTexto(empresa.getActividadPrincipal()) + "): ");
        if (!nuevaActividad.isBlank()) {
            empresa.setActividadPrincipal(nuevaActividad);
        }

        String nuevoEmail = leerTextoOpcional("Nuevo email (" + valorTexto(empresa.getEmail()) + "): ");
        if (!nuevoEmail.isBlank()) {
            empresa.setEmail(nuevoEmail);
        }

        // Domicilio
        System.out.println("Domicilio actual: " +
                (empresa.getDomicilioFiscal() != null ? empresa.getDomicilioFiscal() : "SIN DOMICILIO"));
        System.out.println("Opciones de domicilio:");
        System.out.println("1. Mantener como está");
        System.out.println("2. Quitar domicilio (dejar empresa sin domicilio)");
        System.out.print("Seleccione opción (1-2, Enter para 1): ");
        String linea = scanner.nextLine().trim();

        if (!linea.isEmpty()) {
            int opDom = Integer.parseInt(linea);
            if (opDom == 2) {
                empresa.setDomicilioFiscal(null);
            }
        }

        Empresa actualizada = empresaService.actualizar(empresa);
        System.out.println("Empresa actualizada correctamente:");
        System.out.println(actualizada);
    }

    private void eliminarEmpresa() {
        System.out.println("== Eliminar empresa (baja lógica) ==");

        long id = leerLong("Ingrese ID de la empresa a eliminar: ");

        empresaService.eliminar(id);
        System.out.println("Si existía, la empresa fue marcada como eliminada.");
    }

    // ================= OPCIONES DOMICILIO =================

    private void crearDomicilio() {
        System.out.println("== Crear domicilio fiscal ==");

        DomicilioFiscal d = leerDatosDomicilio();
        DomicilioFiscal creado = domicilioService.insertar(d);

        System.out.println("Domicilio creado correctamente:");
        System.out.println(creado);
    }

    private void listarDomicilios() {
        System.out.println("== Listado de domicilios fiscales ==");

        List<DomicilioFiscal> domicilios = domicilioService.getAll();
        if (domicilios.isEmpty()) {
            System.out.println("No hay domicilios para mostrar.");
            return;
        }

        for (DomicilioFiscal d : domicilios) {
            System.out.println(d);
        }
    }

    private void actualizarDomicilio() {
        System.out.println("== Actualizar domicilio fiscal ==");

        long id = leerLong("Ingrese ID del domicilio a actualizar: ");

        Optional<DomicilioFiscal> opt = domicilioService.getById(id);
        if (opt.isEmpty()) {
            System.out.println("No existe un domicilio con ese ID.");
            return;
        }

        DomicilioFiscal d = opt.get();
        System.out.println("Domicilio actual:");
        System.out.println(d);

        System.out.println("Deje vacío el campo para mantener el valor actual.");

        String nuevaCalle = leerTextoOpcional("Nueva calle (" + d.getCalle() + "): ");
        if (!nuevaCalle.isBlank()) {
            d.setCalle(nuevaCalle);
        }

        System.out.print("Nuevo número (" + (d.getNumero() != null ? d.getNumero() : "NULL") + "): ");
        String linea = scanner.nextLine().trim();
        if (!linea.isEmpty()) {
            try {
                int nuevoNumero = Integer.parseInt(linea);
                d.setNumero(nuevoNumero);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Se mantiene el número actual.");
            }
        }

        DomicilioFiscal actualizado = domicilioService.actualizar(d);
        System.out.println("Domicilio actualizado correctamente:");
        System.out.println(actualizado);
    }

    private void eliminarDomicilio() {
        System.out.println("== Eliminar domicilio fiscal (baja lógica) ==");

        long id = leerLong("Ingrese ID del domicilio a eliminar: ");

        domicilioService.eliminar(id);
        System.out.println("Si existía, el domicilio fue marcado como eliminado.");
    }

    // ================= HELPERS DE ENTRADA =================

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Long.parseLong(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            if (!linea.trim().isEmpty()) {
                return linea.trim();
            }
            System.out.println("El valor no puede ser vacío.");
        }
    }

    private String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        String linea = scanner.nextLine();
        return linea.trim();
    }

    private String valorTexto(String s) {
        return s != null ? s : "NULL";
    }

    private DomicilioFiscal leerDatosDomicilio() {
        String calle = leerTextoObligatorio("Calle: ");

        System.out.print("Número (opcional, Enter para NULL): ");
        String lineaNumero = scanner.nextLine().trim();
        Integer numero = null;
        if (!lineaNumero.isEmpty()) {
            try {
                numero = Integer.parseInt(lineaNumero);
            } catch (NumberFormatException e) {
                System.out.println("Número inválido, se deja en NULL.");
            }
        }

        return new DomicilioFiscal(null, false, calle, numero);
    }

    // Manejo genérico de errores de capa service/DAO
    private void ejecutarConManejoErrores(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}

