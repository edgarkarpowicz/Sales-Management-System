package servicios;

import dao.ClienteDao;
import dao.PersonaDao;
import dao.ConexionSql;
import dao.DomicilioDao;
import dto.ClienteDto;
import dto.PersonaDto;
import dto.DomicilioDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de nivel intermedio para operaciones de administración.
 * Incluye operaciones CRUD para clientes con datos de persona.
 */
public class AdminService {

    /**
     * Retorna todos los clientes con sus datos de persona desde la base de datos.
     * Versión optimizada usando JOIN en lugar de múltiples consultas.
     * @return List<ClienteDto> con todos los clientes y sus datos personales
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public List<ClienteDto> listarClientes() throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        try {
            ClienteDao clienteDao = new ClienteDao(conn);
            return clienteDao.listarClientesConPersona();
        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Versión anterior mantenida para compatibilidad (menos eficiente).
     * @return List<ClienteDto> con todos los clientes y sus datos personales
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public List<ClienteDto> listarClientesLegacy() throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        ClienteDao clienteDao = new ClienteDao(conn);
        PersonaDao personaDao = new PersonaDao(conn);
        List<ClienteDto> resultado = new ArrayList<>();

        try {
            List<ClienteDto> listaClientesDto = clienteDao.listarTodos();

            for (ClienteDto cDto : listaClientesDto) {
                int idPersona = cDto.getIdPersona();
                PersonaDto pDto = personaDao.buscar(new PersonaDto(idPersona));
                
                if (pDto == null) {
                    System.err.println("Persona con ID " + idPersona + " no encontrada para cliente ID " + cDto.getIdCliente());
                    continue;
                }

                ClienteDto clienteCompleto = new ClienteDto(
                    cDto.getIdCliente(),
                    cDto.getIdPersona(),
                    pDto.getNombre(),
                    pDto.getApellido(),
                    pDto.getNumDocumento(),
                    pDto.getTipoDocumento(),
                    pDto.getEmail()
                );

                resultado.add(clienteCompleto);
            }

            return resultado;

        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Busca un cliente específico por su ID incluyendo datos de persona.
     * @param idCliente ID del cliente a buscar
     * @return ClienteDto con datos completos o null si no se encuentra
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public ClienteDto buscarClientePorId(int idCliente) throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        ClienteDao clienteDao = new ClienteDao(conn);
        PersonaDao personaDao = new PersonaDao(conn);

        try {
            ClienteDto cDto = clienteDao.buscar(new ClienteDto(idCliente));
            if (cDto == null) {
                return null;
            }

            PersonaDto pDto = personaDao.buscar(new PersonaDto(cDto.getIdPersona()));
            if (pDto == null) {
                System.err.println("Persona con ID " + cDto.getIdPersona() + " no encontrada para cliente ID " + idCliente);
                return null;
            }

            return new ClienteDto(
                cDto.getIdCliente(),
                cDto.getIdPersona(),
                pDto.getNombre(),
                pDto.getApellido(),
                pDto.getNumDocumento(),
                pDto.getTipoDocumento(),
                pDto.getEmail()
            );

        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Busca clientes por documento (versión optimizada).
     * @param tipoDocumento Tipo de documento (DNI, PASAPORTE, etc.)
     * @param numDocumento Número de documento a buscar
     * @return ClienteDto con datos completos o null si no se encuentra
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public ClienteDto buscarClientePorDocumento(String tipoDocumento, String numDocumento) throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        try {
            ClienteDao clienteDao = new ClienteDao(conn);
            return clienteDao.buscarPorDocumento(tipoDocumento, numDocumento);
        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Sobrecarga del método anterior para compatibilidad hacia atrás.
     * @param numDocumento Número de documento a buscar
     * @return ClienteDto con datos completos o null si no se encuentra
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public ClienteDto buscarClientePorDocumento(String numDocumento) throws SQLException {
        String[] tiposDocumento = {"DNI", "PASAPORTE", "CUIL", "CUIT"};
        
        for (String tipo : tiposDocumento) {
            ClienteDto cliente = buscarClientePorDocumento(tipo, numDocumento);
            if (cliente != null) {
                return cliente;
            }
        }
        
        return null;
    }

    /**
     * Método para obtener un cliente completo con todos sus datos (incluye domicilio).
     * @param idCliente ID del cliente
     * @return ClienteDto con datos completos incluyendo domicilio
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public ClienteDto obtenerClienteCompleto(int idCliente) throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        ClienteDao clienteDao = new ClienteDao(conn);
        PersonaDao personaDao = new PersonaDao(conn);
        DomicilioDao domicilioDao = new DomicilioDao(conn);

        try {
            ClienteDto cDto = clienteDao.buscar(new ClienteDto(idCliente));
            if (cDto == null) {
                return null;
            }

            PersonaDto pDto = personaDao.buscar(new PersonaDto(cDto.getIdPersona()));
            if (pDto == null) {
                System.err.println("Persona con ID " + cDto.getIdPersona() + " no encontrada para cliente ID " + idCliente);
                return null;
            }

            DomicilioDto domDto = null;
            if (pDto.getIdDomicilio() > 0) {
                domDto = domicilioDao.buscar(new DomicilioDto(pDto.getIdDomicilio()));
            }

            ClienteDto clienteCompleto = new ClienteDto(
                cDto.getIdCliente(),
                cDto.getIdPersona(),
                pDto.getNombre(),
                pDto.getApellido(),
                pDto.getNumDocumento(),
                pDto.getTipoDocumento(),
                pDto.getEmail()
            );

            return clienteCompleto;

        } finally {
            conexionSql.cerrar();
        }
    }

    /**
     * Método para insertar un nuevo cliente completo.
     * @param personaDto Datos de la persona a insertar
     * @return ClienteDto del cliente creado o null si falla
     * @throws SQLException en caso de fallo en JDBC/SQLite.
     */
    public ClienteDto insertarCliente(PersonaDto personaDto) throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        try {
            conn.setAutoCommit(false); // Iniciar transacción

            PersonaDao personaDao = new PersonaDao(conn);
            ClienteDao clienteDao = new ClienteDao(conn);

            // 1. Insertar persona
            boolean personaInsertada = personaDao.insertar(personaDto);
            if (!personaInsertada) {
                conn.rollback();
                return null;
            }

            // 2. Insertar cliente
            ClienteDto clienteDto = new ClienteDto();
            clienteDto.setIdPersona(personaDto.getIdPersona());
            boolean clienteInsertado = clienteDao.insertar(clienteDto);
            
            if (!clienteInsertado) {
                conn.rollback();
                return null;
            }

            conn.commit(); // Confirmar transacción

            // 3. Retornar cliente completo
            return new ClienteDto(
                clienteDto.getIdCliente(),
                clienteDto.getIdPersona(),
                personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getNumDocumento(),
                personaDto.getTipoDocumento(),
                personaDto.getEmail()
            );

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conexionSql.cerrar();
        }
    }

    /**
     * Método original mantenido para compatibilidad.
     * @deprecated Usar listarClientes() que incluye datos de persona
     */
    @Deprecated
    public List<ClienteDto> listarClientesOriginal() throws SQLException {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo abrir la conexión a la base de datos.");
        }

        try {
            ClienteDao clienteDao = new ClienteDao(conn);
            return clienteDao.listarTodos();
        } finally {
            conexionSql.cerrar();
        }
    }
}