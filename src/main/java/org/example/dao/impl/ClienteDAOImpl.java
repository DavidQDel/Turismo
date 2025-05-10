package org.example.dao.impl;

import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public void insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nombre_completo, email, telefono) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al insertar, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre_completo = ?, email = ?, telefono = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombreCompleto());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Cliente obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        Cliente cliente = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNombreCompleto(rs.getString("nombre_completo"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setTelefono(rs.getString("telefono"));
                }
            }
        }
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombreCompleto(rs.getString("nombre_completo"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefono(rs.getString("telefono"));

                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public List<Object[]> clientesConMasDeUnaReserva() throws SQLException {
        String sql = "SELECT c.id, c.nombre_completo, COUNT(r.id) as total_reservas " +
                "FROM cliente c " +
                "JOIN reserva r ON c.id = r.id_cliente " +
                "GROUP BY c.id, c.nombre_completo " +
                "HAVING COUNT(r.id) > 1 " +
                "ORDER BY total_reservas DESC";

        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre_completo");
                fila[2] = rs.getInt("total_reservas");
                resultados.add(fila);
            }
        }
        return resultados;
    }
}
