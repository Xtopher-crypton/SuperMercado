package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import controller.Conexao;

public class ProdutoDAO {

    public void cadastrar(Produto p) {
        String sql = "insert into produtos (nome, preco, estoque) values (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQtdEstoque());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public void atualizar(Produto p) {
        String sql = "update produtos set nome = ?, preco = ?, estoque = ? where id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setDouble(2, p.getPreco());
            stmt.setInt(3, p.getQtdEstoque());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sql = "delete from produtos where id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public List<Produto> listarTodos() {
        String sql = "select * from produtos";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("estoque")
                ));
            }

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    public void baixarEstoque(int id, int qtdComprada) {
        String sql = "update produtos set estoque = estoque - ? where id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, qtdComprada);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    // NOVO MÉTODO: Verifica se já existe um produto com o nome, ignorando o ID fornecido
    public boolean existeProdutoPorNome(String nome, int idIgnorado) {
        String sql = "select id from produtos where nome = ? and id != ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, idIgnorado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrou algum registo
            }

        } catch (SQLException e) {
            throw new SupermercadoException("Erro ao verificar nome do produto: " + e.getMessage());
        }
    }
}