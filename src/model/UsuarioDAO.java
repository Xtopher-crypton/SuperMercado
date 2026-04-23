package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import controller.Conexao;

public class UsuarioDAO {

    public String validarLogin(String nome, String cpf) {
        String sql = "select perfil from usuarios where nome = ? and cpf = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("perfil");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
        }
        return null;
    }

    public void cadastrarUsuario(String nome, String cpf, String perfil) {
        String sql = "insert into usuarios (nome, cpf, perfil) values (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, perfil);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuario: " + e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(null, "Erro: CPF ja cadastrado!");
        }
    }
}
