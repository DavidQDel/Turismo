package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class ConexionBD {
    private static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar propiedades
                Properties props = cargarPropiedades();

                // Construir URL de conexión
                String url = construirURL(props);

                // Registrar driver (no necesario desde JDBC 4.0, pero lo mantenemos)
                registrarDriver();

                // Establecer conexión con reintentos
                conexion = establecerConexion(url, props);

            } catch (Exception e) {
                throw new SQLException("Error de conexión: " + e.getMessage(), e);
            }
        }
        return conexion;
    }

    private static Properties cargarPropiedades() throws SQLException {
        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new SQLException("Archivo db.properties no encontrado en classpath");
            }

            Properties props = new Properties();
            props.load(input);
            return props;

        } catch (Exception e) {
            throw new SQLException("Error al leer propiedades: " + e.getMessage(), e);
        }
    }

    private static String construirURL(Properties props) {
        return String.format("jdbc:mysql://%s:%s/%s?useUnicode=%s&useSSL=false&serverTimezone=UTC",
                props.getProperty("host", "localhost"),
                props.getProperty("port", "3306"),
                props.getProperty("database", "turismo"),
                props.getProperty("useUnicode", "yes"));
    }

    private static void registrarDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado. Asegúrate de incluir mysql-connector-java en tus dependencias", e);
        }
    }

    private static Connection establecerConexion(String url, Properties props) throws SQLException {
        System.out.println("Intentando conectar a: " + url.replace("password=.*&", "password=******&"));

        Connection conn = DriverManager.getConnection(
                url,
                props.getProperty("user", "root"),
                props.getProperty("password", "quesada"));

        System.out.println("Conexión exitosa a " + props.getProperty("database", "turismo"));
        return conn;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("Conexión cerrada correctamente");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            } finally {
                conexion = null;
            }
        }
    }
}