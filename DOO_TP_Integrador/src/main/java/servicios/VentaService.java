package servicios;

import dao.ClienteDao;
import dao.PersonaDao;
import dao.ConexionSql;
import dao.DomicilioDao;
import dao.ProductoDao;
import dao.VentaDao;

import dto.ClienteDto;
import dto.PersonaDto;
import dto.DomicilioDto;
import dto.ProductoDto;
import dto.VentaDto;

import modelos.Cliente;
import modelos.Producto;
import modelos.Venta;
import builder.ConcreteBuilderCliente;
import dao.CajeroDao;
import dao.DescuentoDao;
import dao.FacturaDao;
import dto.CajeroDto;
import dto.DescuentoDto;
import enums.Genero;
import enums.TiposDescuentos;
import java.net.URISyntaxException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para listar clientes (solamente). Arma cada Cliente con su
 * respectiva Persona, usando el builder que defines.
 */
public class VentaService {

    /**
     * Lee todos los clientes de la base de datos y devuelve List<Cliente>
     * con los datos completos (Persona mapeada).
     */
    public List<Cliente> obtenerClientes() {
        // 1) Abrir conexión
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            System.err.println("VentaService: error al abrir conexión.");
            return new ArrayList<>();
        }

        // 2) Crear DAO con la misma Connection
        ClienteDao clienteDao = new ClienteDao(conn);
        PersonaDao personaDao = new PersonaDao(conn);
        DomicilioDao domicilioDao = new DomicilioDao(conn);

        List<Cliente> resultado = new ArrayList<>();

        try {
            // 3) Traer todos los ClienteDto
            List<ClienteDto> listaDto = clienteDao.listarTodos();

            for (ClienteDto cDto : listaDto) {
                // 3A) Del ClienteDto obtengo el idPersona
                int idPersona = cDto.getIdPersona();

                // 3B) Busco el PersonaDto por su ID
                PersonaDto pDto = personaDao.buscar(new PersonaDto(idPersona));
                if (pDto == null) {
                    // Si la persona no existe, salto este registro
                    System.err.println("Persona con ID " + idPersona + " no encontrada.");
                    continue;
                }

                // 4) Mapeo usando el builder
                try {
                    ConcreteBuilderCliente builder = new ConcreteBuilderCliente();

                    // Mapear datos de Persona
                    if (pDto.getNombre() != null) {
                        builder.conNombre(pDto.getNombre());
                    }

                    if (pDto.getApellido() != null) {
                        builder.conApellido(pDto.getApellido());
                    }

                    if (pDto.getNumDocumento() != null) {
                        builder.conNumDoc(pDto.getNumDocumento());
                    }

                    if (pDto.getTipoDocumento() != null) {
                        builder.conTipoDoc(pDto.getTipoDocumento());
                    }

                    if (pDto.getCuit() != null) {
                        builder.conCuit(pDto.getCuit());
                    }

                    if (pDto.getCondicionAfip() != null) {
                        builder.conCondAfip(pDto.getCondicionAfip());
                    }

                    // Mapear género - conversión de String a enum Genero
                    if (pDto.getGenero() != null && !pDto.getGenero().trim().isEmpty()) {
                        try {
                            Genero genero = Genero.valueOf(pDto.getGenero().toUpperCase());
                            builder.conGenero(genero);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Género no válido: " + pDto.getGenero() + ". Usando valor por defecto.");
                            // Puedes establecer un género por defecto o dejarlo null
                        }
                    }

                    if (pDto.getFechaNacimiento() != null) {
                        builder.conFechaNac(pDto.getFechaNacimiento());
                    }

                    if (pDto.getEmail() != null) {
                        builder.conEmail(pDto.getEmail());
                    }

                    // Para el domicilio, necesitarías obtener los datos del domicilio
                    // Si tienes un DomicilioDao, puedes hacer algo como:
                    if (pDto.getIdDomicilio() > 0) {
                        DomicilioDto domDto = domicilioDao.buscar(new DomicilioDto(pDto.getIdDomicilio()));
                        if (domDto != null) {
                            if (domDto.getNomCalle() != null) {
                                builder.conNomCalle(domDto.getNomCalle());
                            }
                            if (domDto.getNumVivienda() > 0) {
                                builder.conNumVivienda(domDto.getNumVivienda());
                            }
                            if (domDto.getNumDepartamento() > 0) {
                                builder.conNumDep(domDto.getNumDepartamento());
                            }
                            if (domDto.getCodPostal() > 0) {
                                builder.conCodPos(domDto.getCodPostal());
                            }
                            if (domDto.getLocalidad() != null) {
                                builder.conLoc(domDto.getLocalidad());
                            }
                            if (domDto.getDepartamento() != null) {
                                builder.conDep(domDto.getDepartamento());
                            }
                            if (domDto.getPais() != null) {
                                builder.conPais(domDto.getPais());
                            }
                        }
                    }

                    // Los teléfonos también necesitarían ser obtenidos de otra tabla
                    // Si tienes un TelefonoDao:
                    /*
                    List<String> telefonos = telefonoDao.obtenerTelefonosPorPersona(pDto.getIdPersona());
                    if (telefonos != null && !telefonos.isEmpty()) {
                        builder.conTelefonos(telefonos);
                    }
                     */
                    Cliente cliente = builder.build();
                    resultado.add(cliente);

                } catch (Exception e) {
                    System.err.println("Error al procesar cliente ID " + cDto.getIdCliente() + ": " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }
            }

            return resultado;

        } finally {
            conexionSql.cerrar();
        }
    }

    // ───────────────────────────────────────────────────────────────────────
    // PRODUCTOS (ajustado a tu modelo Producto)
    // ───────────────────────────────────────────────────────────────────────
    /**
     * Obtiene todos los productos de la tabla "Producto" y los mapea a tu clase
     * modelo.
     *
     * @return lista de Productos (vacía si no hay ninguno o hay error)
     */
    public List<Producto> obtenerProductos() {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            System.err.println("VentaService: no se pudo abrir la conexión para productos.");
            return new ArrayList<>();
        }

        ProductoDao productoDao = new ProductoDao(conn);
        List<Producto> resultado = new ArrayList<>();

        try {
            List<ProductoDto> listaDto = productoDao.listarTodos();
            for (ProductoDto dto : listaDto) {
                // Mapear ProductoDto → modelo Producto
                // Obs: tu Producto tiene constructor (String nombre, int stock, float precio, String marca, String codigoBarras)
                Producto p = new Producto(
                        dto.getNombre(),
                        dto.getStock(),
                        (float) dto.getPrecio(), // Si dto.getPrecio() es double, casteamos a float
                        dto.getMarca(),
                        dto.getCodigoBarras()
                );
                resultado.add(p);
            }
            return resultado;
        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Busca todos los productos cuyo nombre contenga la cadena "nombre" (LIKE
     * %nombre%).
     *
     * @param nombre Fragmento de texto a buscar en el campo 'nombre' de la
     * tabla.
     * @return lista de Productos coincidentes (vacía si no hay resultado o hay
     * error)
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            System.err.println("VentaService: no se pudo abrir la conexión para búsqueda por nombre.");
            return new ArrayList<>();
        }

        List<Producto> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE nombre LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto(
                            rs.getString("nombre"),
                            rs.getInt("stock"),
                            rs.getFloat("precio"), // tu modelo usa float
                            rs.getString("marca"),
                            rs.getString("codigoBarras")
                    );
                    resultado.add(p);
                }
            }
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Busca un producto exacto por su código de barras (campo 'codigoBarras').
     *
     * @param codigoBarras Código exacto a buscar.
     * @return Producto encontrado (o null si no existe o hay error).
     */
    public Producto buscarProductoPorCodigo(String codigoBarras) {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            System.err.println("VentaService: no se pudo abrir la conexión para búsqueda por código.");
            return null;
        }

        String sql = "SELECT * FROM Producto WHERE codigoBarras = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoBarras);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getString("nombre"),
                            rs.getInt("stock"),
                            rs.getFloat("precio"),
                            rs.getString("marca"),
                            rs.getString("codigoBarras")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Inserta la venta en la base de datos.
     *
     * @param venta Modelo Venta con datos (incluye cliente con numDocumento, no
     * el id).
     * @return El idVenta generado por SQLite (Autoincrement), o -1 si hubo
     * error.
     * @throws SQLException si ocurre un fallo en la capa JDBC.
     */
    public int insertarVenta(Venta venta) throws SQLException {
        // 1) Abrimos la conexión a SQLite (misma conexión para Cliente y Venta)
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        
        
        if (conn == null) {
            System.err.println("VentaService: no se pudo abrir la conexión para insertar venta.");
            return -1;
        }
     

        try {
            // 2) Obtener el idCliente a partir del nroDocumento del cliente
            String nroDoc = venta.getCliente().getNumDocumento();
            String tipoDoc = venta.getCliente().getTipoDocumento();
            // (Asumimos que en tu modelo Venta tienes venta.getCliente().getNumDocumento())
            ClienteDao clienteDao = new ClienteDao(conn);
            ClienteDto clienteDto = clienteDao.buscarPorDocumento(tipoDoc, nroDoc);

            if (clienteDto == null) {
                // Si no existe el cliente, podrías lanzar excepción o insertar primero el cliente.
                throw new SQLException("No existe un cliente con documento: " + nroDoc);
            }
            int idCliente = clienteDto.getIdCliente();

            // 3) 
            // Conseguimos el Cajero de la DB mediante su numero de documento, tipo de documento, y login.
            String numDocCajero = venta.getCajero().getNumDocumento();
            String tipoDocCajero = venta.getCajero().getTipoDocumento();
            String loginCajero = venta.getCajero().getLogin();

            CajeroDao cajeroDao = new CajeroDao(conn);
            CajeroDto cajeroDto = cajeroDao.buscarPorDocYLogin(tipoDocCajero, numDocCajero, loginCajero);

            // 4) Ahora vamos a querer obtener el Descuento así lo asociamos a la Venta
            String tipo;
            if (venta.getDescuento() != null) {
                tipo = venta.getDescuento().getTipo().name().isEmpty() ? "N/A" : venta.getDescuento().getTipo().name();
            } else {
                tipo = "N/A";
            }

            DescuentoDao descuentoDao = new DescuentoDao(conn);
            DescuentoDto descuentoDto = descuentoDao.buscarPorTipo(tipo);
            // 5) Obtener idFactura (asumimos que la Factura ya se insertó y su id está en el modelo)
            FacturaDao facturaDao = new FacturaDao(conn);
            int facturaId = facturaDao.insertarReturnIdFactura(venta.getFactura().getFechaEmitida(), venta.getFactura().getTotalFactura());

            // 6) Número de tarjeta (opcional)
            String nroTarjeta = venta.getMedioPago().getNumTarjeta() == null || venta.getMedioPago().getNumTarjeta().isEmpty() ? "N/A" : venta.getMedioPago().getNumTarjeta();

            // 7) Tipo de pago y estado
            String tipoTransaccion = venta.getMedioPago().getEstrategiaRecargo().getName();      // ej. "Contado", "Crédito", "Debito"
            String estado = venta.getEstadoVenta().returnName();    // ej. "PAGADA", "PENDIENTE"

            // 8) Creamos el DTO de Venta con todos los campos (idVenta = 0 porque se autogenera)
            VentaDto dto = new VentaDto(
                    idCliente,
                    cajeroDto.getIdCajero(),
                    (descuentoDto == null ? 1 : descuentoDto.getIdDescuento()),
                    facturaId,
                    nroTarjeta,
                    (tipoTransaccion.isEmpty() || tipoTransaccion == null ? "N/A" : tipoTransaccion),
                    estado
            );

            // 9) Insertamos la venta en la BD:
            VentaDao ventaDao = new VentaDao(conn);
            boolean ok = ventaDao.insertar(dto);
            System.out.println("Hola profe, la venta ahora vive en la base de datos.");
            
            if (!ok) {
                return -1;
            }

            // 10) La BD asignó el idVenta al DTO
            return dto.getIdVenta();

        } finally {
            // 11) Cerramos la conexión (importante para SQLite)
            conexionSql.cerrar();
        }
    }

}
