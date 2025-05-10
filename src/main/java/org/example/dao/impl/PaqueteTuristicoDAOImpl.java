package org.example.dao.impl;

import org.example.dao.PaqueteTuristicoDAO;
import org.example.model.PaqueteTuristico;
import org.example.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaqueteTuristicoDAOImpl implements PaqueteTuristicoDAO {
    @Override
    public void insertar(PaqueteTuristico paquete) throws SQLException {
        String sql = "INSERT INTO paquete_turistico (nombre, destino, precio, duracion_dias) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paquete.getNombre());
            stmt.setString(2, paquete.getDestino());
            stmt.setDouble(3, paquete.getPrecio());
            stmt.setInt(4, paquete.getDuracionDias());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al insertar, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paquete.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void actualizar(PaqueteTuristico paquete) throws SQLException {
        String sql = "UPDATE paquete_turistico SET nombre = ?, destino = ?, precio = ?, duracion_dias = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paquete.getNombre());
            stmt.setString(2, paquete.getDestino());
            stmt.setDouble(3, paquete.getPrecio());
            stmt.setInt(4, paquete.getDuracionDias());
            stmt.setInt(5, paquete.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM paquete_turistico WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public PaqueteTuristico obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM paquete_turistico WHERE id = ?";
        PaqueteTuristico paquete = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paquete = new PaqueteTuristico(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("destino"),
                            rs.getDouble("precio"),
                            rs.getInt("duracion_dias")
                    );
                }
            }
        }
        return paquete;
    }

    @Override
    public List<PaqueteTuristico> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM paquete_turistico";
        List<PaqueteTuristico> paquetes = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PaqueteTuristico paquete = new PaqueteTuristico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("destino"),
                        rs.getDouble("precio"),
                        rs.getInt("duracion_dias")
                );
                paquetes.add(paquete);
            }
        }
        return paquetes;
    }

    @Override
    public List<Object[]> rankingDestinosMasReservados() throws SQLException {
        String sql = "SELECT p.destino, COUNT(r.id) as total_reservas " +
                "FROM reserva r " +
                "JOIN paquete_turistico p ON r.id_paquete = p.id " +
                "GROUP BY p.destino " +
                "ORDER BY total_reservas DESC";

        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("destino");
                fila[1] = rs.getInt("total_reservas");
                resultados.add(fila);
            }
        }
        return resultados;
    }
}