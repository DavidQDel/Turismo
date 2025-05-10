package org.example.dao;

import org.example.model.Reserva;
import java.sql.SQLException;
import java.util.List;

public interface ReservaDAO {
    void insertar(Reserva reserva) throws SQLException;
    void actualizar(Reserva reserva) throws SQLException;
    void eliminar(int id) throws SQLException;
    Reserva obtenerPorId(int id) throws SQLException;
    List<Reserva> obtenerTodos() throws SQLException;
    List<Reserva> obtenerPorCliente(int idCliente) throws SQLException;
    void crearReservaConTransaccion(Reserva reserva) throws SQLException;
}
