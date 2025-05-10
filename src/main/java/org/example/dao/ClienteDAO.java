package org.example.dao;

import org.example.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void insertar(Cliente cliente) throws SQLException;
    void actualizar(Cliente cliente) throws SQLException;
    void eliminar(int id) throws SQLException;
    Cliente obtenerPorId(int id) throws SQLException;
    List<Cliente> obtenerTodos() throws SQLException;
    List<Object[]> clientesConMasDeUnaReserva() throws SQLException;
}
