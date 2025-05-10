package org.example;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.model.*;
import org.example.util.ConexionBD;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private static final PaqueteTuristicoDAO paqueteDAO = new PaqueteTuristicoDAOImpl();
    private static final ReservaDAO reservaDAO = new ReservaDAOImpl();
    private static final ActividadDAO actividadDAO = new ActividadDAOImpl();

    public static void main(String[] args) {
        try {
            // Verificar conexión con la base de datos
            ConexionBD.getConexion();
            System.out.println("Conexión a la base de datos establecida correctamente.");

            mostrarMenuPrincipal();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== SISTEMA DE GESTIÓN TURÍSTICA ===");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Paquetes Turísticos");
            System.out.println("3. Gestión de Reservas");
            System.out.println("4. Gestión de Actividades");
            System.out.println("5. Consultas Especiales");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        gestionClientes();
                        break;
                    case 2:
                        gestionPaquetes();
                        break;
                    case 3:
                        gestionReservas();
                        break;
                    case 4:
                        gestionActividades();
                        break;
                    case 5:
                        consultasEspeciales();
                        break;
                    case 6:
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }
    }

    // Menú de gestión de clientes
    private static void gestionClientes() throws SQLException {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n=== GESTIÓN DE CLIENTES ===");
            System.out.println("1. Crear nuevo cliente");
            System.out.println("2. Listar todos los clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    buscarClientePorId();
                    break;
                case 4:
                    actualizarCliente();
                    break;
                case 5:
                    eliminarCliente();
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void crearCliente() throws SQLException {
        System.out.println("\n--- CREAR NUEVO CLIENTE ---");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(0, nombre, email, telefono);
        clienteDAO.insertar(cliente);
        System.out.println("Cliente creado exitosamente con ID: " + cliente.getId());
    }

    private static void listarClientes() throws SQLException {
        System.out.println("\n--- LISTADO DE CLIENTES ---");
        List<Cliente> clientes = clienteDAO.obtenerTodos();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.printf("%-5s %-30s %-25s %-15s%n", "ID", "NOMBRE", "EMAIL", "TELÉFONO");
            clientes.forEach(c -> System.out.printf("%-5d %-30s %-25s %-15s%n",
                    c.getId(), c.getNombreCompleto(), c.getEmail(), c.getTelefono()));
        }
    }

    private static void buscarClientePorId() throws SQLException {
        System.out.print("\nIngrese el ID del cliente a buscar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteDAO.obtenerPorId(id);

        if (cliente != null) {
            System.out.println("\n--- DATOS DEL CLIENTE ---");
            System.out.println("ID: " + cliente.getId());
            System.out.println("Nombre: " + cliente.getNombreCompleto());
            System.out.println("Email: " + cliente.getEmail());
            System.out.println("Teléfono: " + cliente.getTelefono());
        } else {
            System.out.println("No se encontró un cliente con el ID especificado.");
        }
    }

    private static void actualizarCliente() throws SQLException {
        System.out.print("\nIngrese el ID del cliente a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteDAO.obtenerPorId(id);

        if (cliente != null) {
            System.out.println("\n--- ACTUALIZAR CLIENTE ---");
            System.out.println("Datos actuales:");
            System.out.println("Nombre: " + cliente.getNombreCompleto());
            System.out.println("Email: " + cliente.getEmail());
            System.out.println("Teléfono: " + cliente.getTelefono());

            System.out.print("\nNuevo nombre completo (Enter para mantener actual): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) cliente.setNombreCompleto(nombre);

            System.out.print("Nuevo email (Enter para mantener actual): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) cliente.setEmail(email);

            System.out.print("Nuevo teléfono (Enter para mantener actual): ");
            String telefono = scanner.nextLine();
            if (!telefono.isEmpty()) cliente.setTelefono(telefono);

            clienteDAO.actualizar(cliente);
            System.out.println("Cliente actualizado exitosamente.");
        } else {
            System.out.println("No se encontró un cliente con el ID especificado.");
        }
    }

    private static void eliminarCliente() throws SQLException {
        System.out.print("\nIngrese el ID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteDAO.obtenerPorId(id);

        if (cliente != null) {
            System.out.println("\n¿Está seguro que desea eliminar al cliente " + cliente.getNombreCompleto() + "? (S/N)");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                clienteDAO.eliminar(id);
                System.out.println("Cliente eliminado exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } else {
            System.out.println("No se encontró un cliente con el ID especificado.");
        }
    }

    // Menú de gestión de paquetes turísticos (similar al de clientes)
    private static void gestionPaquetes() throws SQLException {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n=== GESTIÓN DE PAQUETES TURÍSTICOS ===");
            System.out.println("1. Crear nuevo paquete");
            System.out.println("2. Listar todos los paquetes");
            System.out.println("3. Buscar paquete por ID");
            System.out.println("4. Actualizar paquete");
            System.out.println("5. Eliminar paquete");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    crearPaquete();
                    break;
                case 2:
                    listarPaquetes();
                    break;
                case 3:
                    buscarPaquetePorId();
                    break;
                case 4:
                    actualizarPaquete();
                    break;
                case 5:
                    eliminarPaquete();
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void crearPaquete() throws SQLException {
        System.out.println("\n--- CREAR NUEVO PAQUETE ---");
        System.out.print("Nombre del paquete: ");
        String nombre = scanner.nextLine();

        System.out.print("Destino: ");
        String destino = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine());

        System.out.print("Duración en días: ");
        int duracion = Integer.parseInt(scanner.nextLine());

        PaqueteTuristico paquete = new PaqueteTuristico(0, nombre, destino, precio, duracion);
        paqueteDAO.insertar(paquete);
        System.out.println("Paquete creado exitosamente con ID: " + paquete.getId());
    }

    private static void listarPaquetes() throws SQLException {
        System.out.println("\n--- LISTADO DE PAQUETES ---");
        List<PaqueteTuristico> paquetes = paqueteDAO.obtenerTodos();

        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados.");
        } else {
            System.out.printf("%-5s %-20s %-20s %-10s %-10s%n", "ID", "NOMBRE", "DESTINO", "PRECIO", "DÍAS");
            paquetes.forEach(p -> System.out.printf("%-5d %-20s %-20s %-10.2f %-10d%n",
                    p.getId(), p.getNombre(), p.getDestino(), p.getPrecio(), p.getDuracionDias()));
        }
    }

    private static void buscarPaquetePorId() throws SQLException {
        System.out.print("\nIngrese el ID del paquete a buscar: ");
        int id = Integer.parseInt(scanner.nextLine());

        PaqueteTuristico paquete = paqueteDAO.obtenerPorId(id);

        if (paquete != null) {
            System.out.println("\n--- DATOS DEL PAQUETE ---");
            System.out.println("ID: " + paquete.getId());
            System.out.println("Nombre: " + paquete.getNombre());
            System.out.println("Destino: " + paquete.getDestino());
            System.out.println("Precio: " + paquete.getPrecio());
            System.out.println("Duración (días): " + paquete.getDuracionDias());
        } else {
            System.out.println("No se encontró un paquete con el ID especificado.");
        }
    }

    private static void actualizarPaquete() throws SQLException {
        System.out.print("\nIngrese el ID del paquete a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        PaqueteTuristico paquete = paqueteDAO.obtenerPorId(id);

        if (paquete != null) {
            System.out.println("\n--- ACTUALIZAR PAQUETE ---");
            System.out.println("Datos actuales:");
            System.out.println("Nombre: " + paquete.getNombre());
            System.out.println("Destino: " + paquete.getDestino());
            System.out.println("Precio: " + paquete.getPrecio());
            System.out.println("Duración (días): " + paquete.getDuracionDias());

            System.out.print("\nNuevo nombre (Enter para mantener actual): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) paquete.setNombre(nombre);

            System.out.print("Nuevo destino (Enter para mantener actual): ");
            String destino = scanner.nextLine();
            if (!destino.isEmpty()) paquete.setDestino(destino);

            System.out.print("Nuevo precio (Enter para mantener actual): ");
            String precioStr = scanner.nextLine();
            if (!precioStr.isEmpty()) paquete.setPrecio(Double.parseDouble(precioStr));

            System.out.print("Nueva duración en días (Enter para mantener actual): ");
            String duracionStr = scanner.nextLine();
            if (!duracionStr.isEmpty()) paquete.setDuracionDias(Integer.parseInt(duracionStr));

            paqueteDAO.actualizar(paquete);
            System.out.println("Paquete actualizado exitosamente.");
        } else {
            System.out.println("No se encontró un paquete con el ID especificado.");
        }
    }

    private static void eliminarPaquete() throws SQLException {
        System.out.print("\nIngrese el ID del paquete a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        PaqueteTuristico paquete = paqueteDAO.obtenerPorId(id);

        if (paquete != null) {
            System.out.println("\n¿Está seguro que desea eliminar el paquete " + paquete.getNombre() + "? (S/N)");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                paqueteDAO.eliminar(id);
                System.out.println("Paquete eliminado exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } else {
            System.out.println("No se encontró un paquete con el ID especificado.");
        }
    }

    // Menú de gestión de reservas
    private static void gestionReservas() throws SQLException {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n=== GESTIÓN DE RESERVAS ===");
            System.out.println("1. Crear nueva reserva");
            System.out.println("2. Listar todas las reservas");
            System.out.println("3. Buscar reserva por ID");
            System.out.println("4. Actualizar estado de reserva");
            System.out.println("5. Cancelar reserva");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    crearReserva();
                    break;
                case 2:
                    listarReservas();
                    break;
                case 3:
                    buscarReservaPorId();
                    break;
                case 4:
                    actualizarEstadoReserva();
                    break;
                case 5:
                    cancelarReserva();
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void crearReserva() throws SQLException {
        System.out.println("\n--- CREAR NUEVA RESERVA ---");

        // Listar clientes para selección
        System.out.println("\nClientes disponibles:");
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Debe crear al menos un cliente primero.");
            return;
        }
        clientes.forEach(c -> System.out.println(c.getId() + " - " + c.getNombreCompleto()));

        System.out.print("\nSeleccione el ID del cliente: ");
        int idCliente = Integer.parseInt(scanner.nextLine());

        // Listar paquetes para selección
        System.out.println("\nPaquetes disponibles:");
        List<PaqueteTuristico> paquetes = paqueteDAO.obtenerTodos();
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados. Debe crear al menos un paquete primero.");
            return;
        }
        paquetes.forEach(p -> System.out.println(p.getId() + " - " + p.getNombre() + " (" + p.getDestino() + ")"));

        System.out.print("\nSeleccione el ID del paquete: ");
        int idPaquete = Integer.parseInt(scanner.nextLine());

        System.out.print("Fecha de reserva (AAAA-MM-DD): ");
        String fechaStr = scanner.nextLine();
        Date fecha = java.sql.Date.valueOf(fechaStr);

        Reserva reserva = new Reserva(0, idCliente, idPaquete, fecha, "PENDIENTE");

        try {
            reservaDAO.crearReservaConTransaccion(reserva);
            System.out.println("Reserva creada exitosamente con ID: " + reserva.getId());
        } catch (SQLException e) {
            System.out.println("Error al crear la reserva: " + e.getMessage());
        }
    }

    private static void listarReservas() throws SQLException {
        System.out.println("\n--- LISTADO DE RESERVAS ---");
        List<Reserva> reservas = reservaDAO.obtenerTodos();

        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            System.out.printf("%-5s %-10s %-10s %-15s %-15s%n", "ID", "ID_CLIENTE", "ID_PAQUETE", "FECHA", "ESTADO");
            reservas.forEach(r -> System.out.printf("%-5d %-10d %-10d %-15s %-15s%n",
                    r.getId(), r.getIdCliente(), r.getIdPaquete(), r.getFechaReserva(), r.getEstado()));
        }
    }

    private static void buscarReservaPorId() throws SQLException {
        System.out.print("\nIngrese el ID de la reserva a buscar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Reserva reserva = reservaDAO.obtenerPorId(id);

        if (reserva != null) {
            System.out.println("\n--- DATOS DE LA RESERVA ---");
            System.out.println("ID: " + reserva.getId());
            System.out.println("ID Cliente: " + reserva.getIdCliente());
            System.out.println("ID Paquete: " + reserva.getIdPaquete());
            System.out.println("Fecha: " + reserva.getFechaReserva());
            System.out.println("Estado: " + reserva.getEstado());
        } else {
            System.out.println("No se encontró una reserva con el ID especificado.");
        }
    }

    private static void actualizarEstadoReserva() throws SQLException {
        System.out.print("\nIngrese el ID de la reserva a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Reserva reserva = reservaDAO.obtenerPorId(id);

        if (reserva != null) {
            System.out.println("\n--- ACTUALIZAR ESTADO DE RESERVA ---");
            System.out.println("Estado actual: " + reserva.getEstado());
            System.out.println("\nEstados disponibles:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADA");
            System.out.println("3. CANCELADA");

            System.out.print("\nSeleccione el nuevo estado (1-3): ");
            int opcion = Integer.parseInt(scanner.nextLine());

            String nuevoEstado = "";
            switch (opcion) {
                case 1:
                    nuevoEstado = "PENDIENTE";
                    break;
                case 2:
                    nuevoEstado = "CONFIRMADA";
                    break;
                case 3:
                    nuevoEstado = "CANCELADA";
                    break;
                default:
                    System.out.println("Opción no válida. No se realizaron cambios.");
                    return;
            }

            reserva.setEstado(nuevoEstado);
            reservaDAO.actualizar(reserva);
            System.out.println("Estado de la reserva actualizado exitosamente a: " + nuevoEstado);
        } else {
            System.out.println("No se encontró una reserva con el ID especificado.");
        }
    }

    private static void cancelarReserva() throws SQLException {
        System.out.print("\nIngrese el ID de la reserva a cancelar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Reserva reserva = reservaDAO.obtenerPorId(id);

        if (reserva != null) {
            System.out.println("\n¿Está seguro que desea cancelar la reserva ID " + reserva.getId() + "? (S/N)");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                reserva.setEstado("CANCELADA");
                reservaDAO.actualizar(reserva);
                System.out.println("Reserva cancelada exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } else {
            System.out.println("No se encontró una reserva con el ID especificado.");
        }
    }

    // Menú de gestión de actividades
    private static void gestionActividades() throws SQLException {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n=== GESTIÓN DE ACTIVIDADES ===");
            System.out.println("1. Crear nueva actividad");
            System.out.println("2. Listar todas las actividades");
            System.out.println("3. Buscar actividad por ID");
            System.out.println("4. Actualizar actividad");
            System.out.println("5. Eliminar actividad");
            System.out.println("6. Listar actividades por paquete");
            System.out.println("7. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    crearActividad();
                    break;
                case 2:
                    listarActividades();
                    break;
                case 3:
                    buscarActividadPorId();
                    break;
                case 4:
                    actualizarActividad();
                    break;
                case 5:
                    eliminarActividad();
                    break;
                case 6:
                    listarActividadesPorPaquete();
                    break;
                case 7:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void crearActividad() throws SQLException {
        System.out.println("\n--- CREAR NUEVA ACTIVIDAD ---");

        // Listar paquetes para selección
        System.out.println("\nPaquetes disponibles:");
        List<PaqueteTuristico> paquetes = paqueteDAO.obtenerTodos();
        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes registrados. Debe crear al menos un paquete primero.");
            return;
        }
        paquetes.forEach(p -> System.out.println(p.getId() + " - " + p.getNombre()));

        System.out.print("\nSeleccione el ID del paquete: ");
        int idPaquete = Integer.parseInt(scanner.nextLine());

        System.out.print("Nombre de la actividad: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Costo adicional: ");
        double costo = Double.parseDouble(scanner.nextLine());

        Actividad actividad = new Actividad(0, idPaquete, nombre, descripcion, costo);
        actividadDAO.insertar(actividad);
        System.out.println("Actividad creada exitosamente con ID: " + actividad.getId());
    }

    private static void listarActividades() throws SQLException {
        System.out.println("\n--- LISTADO DE ACTIVIDADES ---");
        List<Actividad> actividades = actividadDAO.obtenerTodos();

        if (actividades.isEmpty()) {
            System.out.println("No hay actividades registradas.");
        } else {
            System.out.printf("%-5s %-10s %-20s %-30s %-10s%n", "ID", "ID_PAQUETE", "NOMBRE", "DESCRIPCIÓN", "COSTO");
            actividades.forEach(a -> System.out.printf("%-5d %-10d %-20s %-30s %-10.2f%n",
                    a.getId(), a.getIdPaquete(), a.getNombre(),
                    a.getDescripcion().substring(0, Math.min(a.getDescripcion().length(), 25)) + "...",
                    a.getCostoAdicional()));
        }
    }

    private static void buscarActividadPorId() throws SQLException {
        System.out.print("\nIngrese el ID de la actividad a buscar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Actividad actividad = actividadDAO.obtenerPorId(id);

        if (actividad != null) {
            System.out.println("\n--- DATOS DE LA ACTIVIDAD ---");
            System.out.println("ID: " + actividad.getId());
            System.out.println("ID Paquete: " + actividad.getIdPaquete());
            System.out.println("Nombre: " + actividad.getNombre());
            System.out.println("Descripción: " + actividad.getDescripcion());
            System.out.println("Costo adicional: " + actividad.getCostoAdicional());
        } else {
            System.out.println("No se encontró una actividad con el ID especificado.");
        }
    }

    private static void actualizarActividad() throws SQLException {
        System.out.print("\nIngrese el ID de la actividad a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Actividad actividad = actividadDAO.obtenerPorId(id);

        if (actividad != null) {
            System.out.println("\n--- ACTUALIZAR ACTIVIDAD ---");
            System.out.println("Datos actuales:");
            System.out.println("ID Paquete: " + actividad.getIdPaquete());
            System.out.println("Nombre: " + actividad.getNombre());
            System.out.println("Descripción: " + actividad.getDescripcion());
            System.out.println("Costo adicional: " + actividad.getCostoAdicional());

            System.out.print("\nNuevo ID de paquete (Enter para mantener actual): ");
            String idPaqueteStr = scanner.nextLine();
            if (!idPaqueteStr.isEmpty()) actividad.setIdPaquete(Integer.parseInt(idPaqueteStr));

            System.out.print("Nuevo nombre (Enter para mantener actual): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) actividad.setNombre(nombre);

            System.out.print("Nueva descripción (Enter para mantener actual): ");
            String descripcion = scanner.nextLine();
            if (!descripcion.isEmpty()) actividad.setDescripcion(descripcion);

            System.out.print("Nuevo costo adicional (Enter para mantener actual): ");
            String costoStr = scanner.nextLine();
            if (!costoStr.isEmpty()) actividad.setCostoAdicional(Double.parseDouble(costoStr));

            actividadDAO.actualizar(actividad);
            System.out.println("Actividad actualizada exitosamente.");
        } else {
            System.out.println("No se encontró una actividad con el ID especificado.");
        }
    }

    private static void eliminarActividad() throws SQLException {
        System.out.print("\nIngrese el ID de la actividad a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Actividad actividad = actividadDAO.obtenerPorId(id);

        if (actividad != null) {
            System.out.println("\n¿Está seguro que desea eliminar la actividad " + actividad.getNombre() + "? (S/N)");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                actividadDAO.eliminar(id);
                System.out.println("Actividad eliminada exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } else {
            System.out.println("No se encontró una actividad con el ID especificado.");
        }
    }

    private static void listarActividadesPorPaquete() throws SQLException {
        System.out.print("\nIngrese el ID del paquete para listar sus actividades: ");
        int idPaquete = Integer.parseInt(scanner.nextLine());

        List<Actividad> actividades = actividadDAO.obtenerPorPaquete(idPaquete);

        if (actividades.isEmpty()) {
            System.out.println("No hay actividades registradas para este paquete.");
        } else {
            System.out.println("\n--- ACTIVIDADES DEL PAQUETE " + idPaquete + " ---");
            System.out.printf("%-5s %-20s %-30s %-10s%n", "ID", "NOMBRE", "DESCRIPCIÓN", "COSTO");
            actividades.forEach(a -> System.out.printf("%-5d %-20s %-30s %-10.2f%n",
                    a.getId(), a.getNombre(),
                    a.getDescripcion().substring(0, Math.min(a.getDescripcion().length(), 25)) + "...",
                    a.getCostoAdicional()));
        }
    }

    // Menú de consultas especiales
    private static void consultasEspeciales() throws SQLException {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n=== CONSULTAS ESPECIALES ===");
            System.out.println("1. Ranking de destinos más reservados");
            System.out.println("2. Clientes con más de una reserva");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    mostrarRankingDestinos();
                    break;
                case 2:
                    mostrarClientesFrecuentes();
                    break;
                case 3:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void mostrarRankingDestinos() throws SQLException {
        System.out.println("\n--- RANKING DE DESTINOS MÁS RESERVADOS ---");
        List<Object[]> ranking = paqueteDAO.rankingDestinosMasReservados();

        if (ranking.isEmpty()) {
            System.out.println("No hay datos suficientes para generar el ranking.");
        } else {
            System.out.printf("%-20s %-15s%n", "DESTINO", "RESERVAS");
            ranking.forEach(r -> System.out.printf("%-20s %-15d%n", r[0], r[1]));
        }
    }

    private static void mostrarClientesFrecuentes() throws SQLException {
        System.out.println("\n--- CLIENTES CON MÁS DE UNA RESERVA ---");
        List<Object[]> clientes = clienteDAO.clientesConMasDeUnaReserva();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes con más de una reserva.");
        } else {
            System.out.printf("%-5s %-30s %-10s%n", "ID", "NOMBRE", "RESERVAS");
            clientes.forEach(c -> System.out.printf("%-5d %-30s %-10d%n", c[0], c[1], c[2]));
        }
    }
}