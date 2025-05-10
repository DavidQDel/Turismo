package org.example.dao;

import org.example.model.Actividad;
import java.sql.SQLException;
import java.util.List;

public interface ActividadDAO {
    void insertar(Actividad actividad) throws SQLException;
    void actualizar(Actividad actividad) throws SQLException;
    void eliminar(int id) throws SQLException;
    Actividad obtenerPorId(int id) throws SQLException;
    List<Actividad> obtenerTodos() throws SQLException;
    List<Actividad> obtenerPorPaquete(int idPaquete) throws SQLException;
}
