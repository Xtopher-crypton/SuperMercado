package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import model.SupermercadoException;

public class Conexao {
    private static final String URL  = "jdbc:mysql://localhost:3306/db_supermercado";
    private static final String USER = "root";
    private static final String PASS = "DHQje6ff-NF2K2y";

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            // Lança a exceção em vez de apenas imprimir no console
            throw new SupermercadoException("Erro ao conectar com a base de dados: " + e.getMessage());
        }
    }
}