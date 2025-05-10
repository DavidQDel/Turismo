package org.example.dao.impl;

import org.example.dao.ReservaDAO;
import org.example.model.Reserva;
import org.example.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {
    @Override
    public void insertar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (id_cliente, id_paquete, fecha_reserva, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdPaquete());
            stmt.setDate(3, new java.sql.Date(reserva.getFechaReserva().getTime()));
            stmt.setString(4, reserva.getEstado());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al insertar, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reserva.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Reserva reserva) throws SQLException {
        String sql = "UPDATE reserva SET id_cliente = ?, id_paquete = ?, fecha_reserva = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdPaquete());
            stmt.setDate(3, new java.sql.Date(reserva.getFechaReserva().getTime()));
            stmt.setString(4, reserva.getEstado());
            stmt.setInt(5, reserva.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM reserva WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Reserva obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE id = ?";
        Reserva reserva = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reserva = new Reserva(
                            rs.getInt("id"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_paquete"),
                            rs.getDate("fecha_reserva"),
                            rs.getString("estado")
                    );
                }
            }
        }
        return reserva;
    }

    @Override
    public List<Reserva> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM reserva";
        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("id"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_paquete"),
                        rs.getDate("fecha_reserva"),
                        rs.getString("estado")
                );
                reservas.add(reserva);
            }
        }
        return reservas;
    }

    @Override
    public List<Reserva> obtenerPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE id_cliente = ?";
        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva(
                            rs.getInt("id"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_paquete"),
                            rs.getDate("fecha_reserva"),
                            rs.getString("estado")
                    );
                    reservas.add(reserva);
                }
            }
        }
        return reservas;
    }

    @Override
    public void crearReservaConTransaccion(Reserva reserva) throws SQLException {
        Connection conn = null;
        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Validar disponibilidad (ejemplo simplificado)
            String sqlValidacion = "SELECT COUNT(*) FROM paquete_turistico WHERE id = ?";
            try (PreparedStatement stmtValidacion = conn.prepareStatement(sqlValidacion)) {
                stmtValidacion.setInt(1, reserva.getIdPaquete());
                try (ResultSet rs = stmtValidacion.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new SQLException("El paquete turístico no existe");
                    }
                }
            }

            // 2. Insertar la reserva
            String sqlInsert = "INSERT INTO reserva (id_cliente, id_paquete, fecha_reserva, estado) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsert.setInt(1, reserva.getIdCliente());
                stmtInsert.setInt(2, reserva.getIdPaquete());
                stmtInsert.setDate(3, new java.sql.Date(reserva.getFechaReserva().getTime()));
                stmtInsert.setString(4, reserva.getEstado());

                int affectedRows = stmtInsert.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Error al insertar reserva, ninguna fila afectada.");
                }

                try (ResultSet generatedKeys = stmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reserva.setId(generatedKeys.getInt(1));
                    }
                }
            }

            conn.commit(); // Confirmar transacción si todo va bien
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    throw new SQLException("Error al hacer rollback", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar autocommit
                    conn.close();
                } catch (SQLException e) {
                    // Log del error
                }
            }
        }
    }
}
