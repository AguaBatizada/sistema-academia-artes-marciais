package dao;

import conexao.ConexaoBanco;
import model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void inserir(Aluno a) {
        String sql = "INSERT INTO Alunos (nome, email, telefone, faixa, cpf) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getEmail());
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getFaixa());
            stmt.setString(5, a.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Aluno a) {
        String sql = "UPDATE Alunos SET nome = ?, email = ?, telefone = ?, faixa = ?, cpf = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getNome());
            stmt.setString(2, a.getEmail());
            stmt.setString(3, a.getTelefone());
            stmt.setString(4, a.getFaixa());
            stmt.setString(5, a.getCpf());
            stmt.setInt(6, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM Alunos WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aluno> listarTodos(String ordenacao, String filtroNome) {
        List<Aluno> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Alunos WHERE 1=1");

        if (filtroNome != null && !filtroNome.trim().isEmpty()) {
            sql.append(" AND nome LIKE ?");
        }

        if ("nome".equals(ordenacao)) {
            sql.append(" ORDER BY nome ASC");
        } else if ("faixa".equals(ordenacao)) {
            sql.append(" ORDER BY faixa ASC");
        } else {
            sql.append(" ORDER BY id ASC");
        }

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (filtroNome != null && !filtroNome.trim().isEmpty()) {
                stmt.setString(1, "%" + filtroNome + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno a = new Aluno(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("faixa"),
                        rs.getString("cpf")
                    );
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
