package dao;

import dto.ClienteDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupoLudeña
 */
public class ClienteDao implements Dao<ClienteDto> {

    private final Connection conn; // Managed via ConexionSql

    public ClienteDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ClienteDto buscar(ClienteDto dto) { // Existing method
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdCliente());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCliente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClienteDto buscarPorDocumento(String tipoDoc, String nroDoc) {
        String sql = "SELECT c.*, p.* "
                + "FROM Cliente c "
                + "JOIN Persona p ON c.idPersona = p.idPersona "
                + "WHERE p.tipoDocumento = ? AND p.numDocumento = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipoDoc);
            stmt.setString(2, nroDoc);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToClienteCompleto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Nuevo método para listar todos los clientes con datos de persona usando JOIN
     */
    public List<ClienteDto> listarClientesConPersona() {
        List<ClienteDto> lista = new ArrayList<>();
        String sql = "SELECT c.idCliente, c.idPersona, p.nombre, p.apellido, " +
                     "p.numDocumento, p.tipoDocumento, p.email " +
                     "FROM Cliente c " +
                     "JOIN Persona p ON c.idPersona = p.idPersona " +
                     "ORDER BY p.apellido, p.nombre";

        
        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToClienteCompleto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<ClienteDto> listarTodos() { // Existing method
        List<ClienteDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insertar(ClienteDto dto) { // Adapted from existing
        String sql = "INSERT INTO Cliente (idPersona) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dto.getIdPersona());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setIdCliente(generatedKeys.getInt(1));
                } else {
                    System.err.println("Creating Cliente failed, no ID obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(ClienteDto dto) { // Existing method
        String sql = "UPDATE Cliente SET idPersona = ? WHERE idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdPersona());
            stmt.setInt(2, dto.getIdCliente());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrar(ClienteDto dto) { // Existing method
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdCliente());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ClienteDto> listarPorCriterio(ClienteDto dto) { // Adapted from existing
        // Example: Search by idPersona if provided
        List<ClienteDto> lista = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Cliente WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dto.getIdPersona() > 0) { // Assuming ID is positive
            sqlBuilder.append(" AND idPersona = ?");
            params.add(dto.getIdPersona());
        }
        // Add other criteria as needed

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método original para Cliente básico
    private ClienteDto mapResultSetToCliente(ResultSet rs) throws SQLException {
        return new ClienteDto(
                rs.getInt("idCliente"),
                rs.getInt("idPersona")
        );
    }

    // Nuevo método para Cliente con datos de Persona
    private ClienteDto mapResultSetToClienteCompleto(ResultSet rs) throws SQLException {
        return new ClienteDto(
                rs.getInt("idCliente"),
                rs.getInt("idPersona"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("numDocumento"),
                rs.getString("tipoDocumento"),
                rs.getString("email")
        );
    }
}