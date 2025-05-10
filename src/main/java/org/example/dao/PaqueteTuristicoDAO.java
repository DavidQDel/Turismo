package org.example.dao;

import org.example.model.PaqueteTuristico;
import java.sql.SQLException;
import java.util.List;

public interface PaqueteTuristicoDAO {
    void insertar(PaqueteTuristico paquete) throws SQLException;
    void actualizar(PaqueteTuristico paquete) throws SQLException;
    void eliminar(int id) throws SQLException;
    PaqueteTuristico obtenerPorId(int id) throws SQLException;
    List<PaqueteTuristico> obtenerTodos() throws SQLException;
    List<Object[]> rankingDestinosMasReservados() throws SQLException;
}