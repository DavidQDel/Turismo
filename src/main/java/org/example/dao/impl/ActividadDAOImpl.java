package org.example.dao.impl;

import org.example.dao.ActividadDAO;
import org.example.model.Actividad;
import org.example.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAOImpl implements ActividadDAO {
    @Override
    public void insertar(Actividad actividad) throws SQLException {
        String sql = "INSERT INTO actividad (id_paquete, nombre, descripcion, costo_adicional) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, actividad.getIdPaquete());
            stmt.setString(2, actividad.getNombre());
            stmt.setString(3, actividad.getDescripcion());
            stmt.setDouble(4, actividad.getCostoAdicional());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al insertar, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    actividad.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void actualizar(Actividad actividad) throws SQLException {
        String sql = "UPDATE actividad SET id_paquete = ?, nombre = ?, descripcion = ?, costo_adicional = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, actividad.getIdPaquete());
            stmt.setString(2, actividad.getNombre());
            stmt.setString(3, actividad.getDescripcion());
            stmt.setDouble(4, actividad.getCostoAdicional());
            stmt.setInt(5, actividad.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM actividad WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Actividad obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM actividad WHERE id = ?";
        Actividad actividad = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    actividad = new Actividad(
                            rs.getInt("id"),
                            rs.getInt("id_paquete"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("costo_adicional")
                    );
                }
            }
        }
        return actividad;
    }

    @Override
    public List<Actividad> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM actividad";
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Actividad actividad = new Actividad(
                        rs.getInt("id"),
                        rs.getInt("id_paquete"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("costo_adicional")
                );
                actividades.add(actividad);
            }
        }
        return actividades;
    }

    @Override
    public List<Actividad> obtenerPorPaquete(int idPaquete) throws SQLException {
        String sql = "SELECT * FROM actividad WHERE id_paquete = ?";
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaquete);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actividad actividad = new Actividad(
                            rs.getInt("id"),
                            rs.getInt("id_paquete"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("costo_adicional")
                    );
                    actividades.add(actividad);
                }
            }
        }
        return actividades;
    }
}