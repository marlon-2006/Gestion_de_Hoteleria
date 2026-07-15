package model;

import exception.HabitacionNoDisponibleException;
import exception.ReservaInvalidaException;
import model.enums.EstadoHabitacion;
import model.enums.EstadoReserva;
import model.enums.TipoServicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Persistencia del sistema mediante archivos de texto.
 * Los datos se guardan en la carpeta "datos" en cuatro archivos .txt.
 */
public final class DataStore {

    private static final Path CARPETA = Paths.get("datos");
    private static final Path HABITACIONES = CARPETA.resolve("habitaciones.txt");
    private static final Path EMPLEADOS = CARPETA.resolve("empleados.txt");
    private static final Path CLIENTES = CARPETA.resolve("clientes.txt");
    private static final Path RESERVAS = CARPETA.resolve("reservas.txt");
    private static final String FORMATO_FECHA = "yyyy-MM-dd";

    private DataStore() {
    }

    public static HotelJWMarriottQuito cargar() {
        if (!existenArchivos()) {
            HotelJWMarriottQuito hotelInicial = crearDatosIniciales();
            guardar(hotelInicial);
            return hotelInicial;
        }

        try {
            HotelJWMarriottQuito hotel = new HotelJWMarriottQuito();
            Map<Integer, EstadoHabitacion> estadosGuardados = cargarHabitaciones(hotel);
            cargarEmpleados(hotel);
            cargarClientes(hotel);
            cargarReservas(hotel);

            // Se aplica al final para conservar estados como LIMPIEZA.
            for (Habitacion habitacion : hotel.getHabitaciones()) {
                EstadoHabitacion estado = estadosGuardados.get(habitacion.getNumero());
                if (estado != null) {
                    habitacion.cambiarEstado(estado);
                }
            }

            completarHabitaciones(hotel);
            guardar(hotel);
            return hotel;

        } catch (IOException | RuntimeException ex) {
            System.err.println("No se pudieron cargar los archivos de texto: " + ex.getMessage());
            HotelJWMarriottQuito hotelInicial = crearDatosIniciales();
            guardar(hotelInicial);
            return hotelInicial;
        }
    }

    public static void guardar(HotelJWMarriottQuito hotel) {
        try {
            Files.createDirectories(CARPETA);
            guardarHabitaciones(hotel);
            guardarEmpleados(hotel);
            guardarClientes(hotel);
            guardarReservas(hotel);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No se pudieron guardar los datos en archivos .txt: " + ex.getMessage(), ex);
        }
    }

    private static boolean existenArchivos() {
        return Files.exists(HABITACIONES)
                && Files.exists(EMPLEADOS)
                && Files.exists(CLIENTES)
                && Files.exists(RESERVAS);
    }

    private static BufferedWriter escritor(Path archivo) throws IOException {
        return Files.newBufferedWriter(
                archivo,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    private static void guardarHabitaciones(HotelJWMarriottQuito hotel) throws IOException {
        try (BufferedWriter writer = escritor(HABITACIONES)) {
            writer.write("# tipo|numero|precioBase|estado|caracteristica");
            writer.newLine();

            for (Habitacion habitacion : hotel.getHabitaciones()) {
                String tipo;
                String caracteristica = "";

                if (habitacion instanceof SuitePresidencial) {
                    tipo = "SUITE_PRESIDENCIAL";
                    caracteristica = Boolean.toString(
                            ((SuitePresidencial) habitacion).isIncluyeServicioVIP());
                } else if (habitacion instanceof SuiteEjecutiva) {
                    tipo = "SUITE_EJECUTIVA";
                    caracteristica = Boolean.toString(
                            ((SuiteEjecutiva) habitacion).isIncluyeDesayuno());
                } else if (habitacion instanceof HabitacionDoble) {
                    tipo = "DOBLE";
                } else {
                    tipo = "SIMPLE";
                }

                writer.write(String.join("|",
                        tipo,
                        Integer.toString(habitacion.getNumero()),
                        Double.toString(habitacion.getPrecioPorNoche()),
                        habitacion.getEstado().name(),
                        caracteristica));
                writer.newLine();
            }
        }
    }

    private static Map<Integer, EstadoHabitacion> cargarHabitaciones(
            HotelJWMarriottQuito hotel) throws IOException {

        Map<Integer, EstadoHabitacion> estados = new HashMap<>();

        for (String[] datos : leerRegistros(HABITACIONES)) {
            if (datos.length < 4) {
                continue;
            }

            String tipo = datos[0];
            int numero = Integer.parseInt(datos[1]);
            double precio = Double.parseDouble(datos[2]);
            EstadoHabitacion estado = EstadoHabitacion.valueOf(datos[3]);
            boolean caracteristica = datos.length > 4 && Boolean.parseBoolean(datos[4]);

            Habitacion habitacion;
            switch (tipo) {
                case "DOBLE":
                    habitacion = new HabitacionDoble(numero, precio);
                    break;
                case "SUITE_EJECUTIVA":
                    habitacion = new SuiteEjecutiva(numero, precio, caracteristica);
                    break;
                case "SUITE_PRESIDENCIAL":
                    habitacion = new SuitePresidencial(numero, precio, caracteristica);
                    break;
                default:
                    habitacion = new HabitacionSimple(numero, precio);
                    break;
            }

            hotel.agregarHabitacion(habitacion);
            estados.put(numero, estado);
        }

        return estados;
    }

    private static void guardarEmpleados(HotelJWMarriottQuito hotel) throws IOException {
        try (BufferedWriter writer = escritor(EMPLEADOS)) {
            writer.write("# tipo|nombre|apellido|cedula|telefono|salario|turno|codigo|datoEspecifico");
            writer.newLine();

            for (Empleado empleado : hotel.getEmpleados()) {
                String tipo;
                String especifico;

                if (empleado instanceof Administrador) {
                    tipo = "ADMINISTRADOR";
                    especifico = Integer.toString(((Administrador) empleado).getNivelAcceso());
                } else if (empleado instanceof Recepcionista) {
                    tipo = "RECEPCIONISTA";
                    especifico = ((Recepcionista) empleado).getNumExtension();
                } else {
                    tipo = "HOUSEKEEPER";
                    especifico = ((Housekeeper) empleado).getZona();
                }

                writer.write(String.join("|",
                        tipo,
                        codificar(empleado.getNombre()),
                        codificar(empleado.getApellido()),
                        codificar(empleado.getCedula()),
                        codificar(empleado.getTelefono()),
                        Double.toString(empleado.getSalario()),
                        codificar(empleado.getTurno()),
                        codificar(empleado.getCodigoEmp()),
                        codificar(especifico)));
                writer.newLine();
            }
        }
    }

    private static void cargarEmpleados(HotelJWMarriottQuito hotel) throws IOException {
        for (String[] datos : leerRegistros(EMPLEADOS)) {
            if (datos.length < 9) {
                continue;
            }

            String tipo = datos[0];
            String nombre = decodificar(datos[1]);
            String apellido = decodificar(datos[2]);
            String cedula = decodificar(datos[3]);
            String telefono = decodificar(datos[4]);
            double salario = Double.parseDouble(datos[5]);
            String turno = decodificar(datos[6]);
            String codigo = decodificar(datos[7]);
            String especifico = decodificar(datos[8]);

            Empleado empleado;
            switch (tipo) {
                case "ADMINISTRADOR":
                    empleado = new Administrador(nombre, apellido, cedula, telefono,
                            salario, turno, codigo, Integer.parseInt(especifico));
                    break;
                case "HOUSEKEEPER":
                    empleado = new Housekeeper(nombre, apellido, cedula, telefono,
                            salario, turno, codigo, especifico);
                    break;
                default:
                    empleado = new Recepcionista(nombre, apellido, cedula, telefono,
                            salario, turno, codigo, especifico);
                    break;
            }
            hotel.registrarEmpleado(empleado);
        }
    }

    private static void guardarClientes(HotelJWMarriottQuito hotel) throws IOException {
        try (BufferedWriter writer = escritor(CLIENTES)) {
            writer.write("# nombre|apellido|cedula|telefono|email|tarjeta");
            writer.newLine();

            for (Cliente cliente : hotel.getClientes()) {
                writer.write(String.join("|",
                        codificar(cliente.getNombre()),
                        codificar(cliente.getApellido()),
                        codificar(cliente.getCedula()),
                        codificar(cliente.getTelefono()),
                        codificar(cliente.getEmail()),
                        codificar(cliente.getNumTarjeta())));
                writer.newLine();
            }
        }
    }

    private static void cargarClientes(HotelJWMarriottQuito hotel) throws IOException {
        for (String[] datos : leerRegistros(CLIENTES)) {
            if (datos.length < 6) {
                continue;
            }

            hotel.registrarCliente(new Cliente(
                    decodificar(datos[0]),
                    decodificar(datos[1]),
                    decodificar(datos[2]),
                    decodificar(datos[3]),
                    decodificar(datos[4]),
                    decodificar(datos[5])));
        }
    }

    private static void guardarReservas(HotelJWMarriottQuito hotel) throws IOException {
        try (BufferedWriter writer = escritor(RESERVAS)) {
            writer.write("# id|cedulaCliente|habitacion|fechaEntrada|fechaSalida|estado|descuento|servicios");
            writer.newLine();

            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            formatoFecha.setLenient(false);

            for (Reserva reserva : hotel.getReservas()) {
                StringBuilder servicios = new StringBuilder();
                for (Servicio servicio : reserva.getServiciosAdicionales()) {
                    if (servicios.length() > 0) {
                        servicios.append(';');
                    }
                    servicios.append(codificar(servicio.getIdServicio())).append(',')
                            .append(codificar(servicio.getNombre())).append(',')
                            .append(servicio.getCosto()).append(',')
                            .append(servicio.getTipo().name());
                }

                writer.write(String.join("|",
                        codificar(reserva.getIdReserva()),
                        codificar(reserva.getCliente().getCedula()),
                        Integer.toString(reserva.getHabitacion().getNumero()),
                        formatoFecha.format(reserva.getFechaEntrada()),
                        formatoFecha.format(reserva.getFechaSalida()),
                        reserva.getEstado().name(),
                        Double.toString(reserva.getDescuento()),
                        servicios.toString()));
                writer.newLine();
            }
        }
    }

    private static void cargarReservas(HotelJWMarriottQuito hotel) throws IOException {
        for (String[] datos : leerRegistros(RESERVAS)) {
            if (datos.length < 7) {
                continue;
            }

            Cliente cliente = buscarCliente(hotel, decodificar(datos[1]));
            Habitacion habitacion = buscarHabitacion(hotel, Integer.parseInt(datos[2]));
            if (cliente == null || habitacion == null) {
                continue;
            }

            try {
                Reserva reserva = new Reserva(
                        decodificar(datos[0]),
                        cliente,
                        habitacion,
                        convertirFecha(datos[3]),
                        convertirFecha(datos[4]));

                reserva.setDescuento(Double.parseDouble(datos[6]));

                if (datos.length > 7 && !datos[7].isBlank()) {
                    for (String registroServicio : datos[7].split(";")) {
                        String[] servicio = registroServicio.split(",", -1);
                        if (servicio.length == 4) {
                            reserva.agregarServicio(new Servicio(
                                    decodificar(servicio[0]),
                                    decodificar(servicio[1]),
                                    Double.parseDouble(servicio[2]),
                                    TipoServicio.valueOf(servicio[3])));
                        }
                    }
                }

                hotel.getReservas().add(reserva);
                EstadoReserva estado = EstadoReserva.valueOf(datos[5]);
                if (estado == EstadoReserva.CONFIRMADA) {
                    reserva.confirmar();
                } else if (estado == EstadoReserva.CANCELADA) {
                    reserva.cancelar();
                }

            } catch (ReservaInvalidaException | HabitacionNoDisponibleException ex) {
                System.err.println("Reserva omitida al leer reservas.txt: " + ex.getMessage());
            }
        }
    }

    private static Date convertirFecha(String texto) {
        SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
        formato.setLenient(false);

        try {
            return formato.parse(texto);
        } catch (ParseException ex) {
            // Compatibilidad con archivos anteriores que guardaban milisegundos.
            try {
                return new Date(Long.parseLong(texto));
            } catch (NumberFormatException numeroEx) {
                throw new IllegalArgumentException(
                        "Fecha inválida en reservas.txt: " + texto, numeroEx);
            }
        }
    }

    private static List<String[]> leerRegistros(Path archivo) throws IOException {
        List<String[]> registros = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return registros;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                registros.add(linea.split("\\|", -1));
            }
        }
        return registros;
    }

    private static Cliente buscarCliente(HotelJWMarriottQuito hotel, String cedula) {
        for (Cliente cliente : hotel.getClientes()) {
            if (cliente.getCedula().equals(cedula)) {
                return cliente;
            }
        }
        return null;
    }

    private static Habitacion buscarHabitacion(HotelJWMarriottQuito hotel, int numero) {
        for (Habitacion habitacion : hotel.getHabitaciones()) {
            if (habitacion.getNumero() == numero) {
                return habitacion;
            }
        }
        return null;
    }

    private static String codificar(String texto) {
        if (texto == null) {
            return "";
        }
        // Evita romper el separador de columnas y mantiene el archivo legible.
        return texto.replace("|", "/")
                .replace("\r", " ")
                .replace("\n", " ");
    }

    private static String decodificar(String texto) {
        return texto == null ? "" : texto;
    }

    private static void completarHabitaciones(HotelJWMarriottQuito hotel) {
        for (int numero = 101; numero <= 110; numero++) {
            agregarSiNoExiste(hotel, new HabitacionSimple(numero, 90.00));
        }
        for (int numero = 201; numero <= 208; numero++) {
            agregarSiNoExiste(hotel, new HabitacionDoble(numero, 140.00));
        }
        for (int numero = 301; numero <= 304; numero++) {
            agregarSiNoExiste(hotel, new SuiteEjecutiva(numero, 220.00, true));
        }
        for (int numero = 401; numero <= 402; numero++) {
            agregarSiNoExiste(hotel, new SuitePresidencial(numero, 500.00, true));
        }
    }

    private static void agregarSiNoExiste(HotelJWMarriottQuito hotel, Habitacion nueva) {
        for (Habitacion existente : hotel.getHabitaciones()) {
            if (existente.getNumero() == nueva.getNumero()) {
                return;
            }
        }
        hotel.agregarHabitacion(nueva);
    }

    public static HotelJWMarriottQuito crearDatosIniciales() {
        HotelJWMarriottQuito hotel = new HotelJWMarriottQuito();
        completarHabitaciones(hotel);
        hotel.registrarEmpleado(new Administrador(
                "Ana", "Torres", "1711111111", "0991111111",
                1600, "Mañana", "ADM001", 3));
        hotel.registrarEmpleado(new Recepcionista(
                "Carlos", "Pérez", "1722222222", "0992222222",
                900, "Mañana", "REC001", "101"));
        hotel.registrarEmpleado(new Housekeeper(
                "Luisa", "Mena", "1733333333", "0993333333",
                750, "Mañana", "HK001", "Pisos 1-3"));
        return hotel;
    }
}
