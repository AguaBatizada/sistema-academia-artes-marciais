package dao;

import conexao.ConexaoBanco;
import model.Matricula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {

    public void inserir(Matricula m) {
        String sql = "INSERT INTO Matriculas (aluno_id, modalidade_id, status) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getAlunoId());
            stmt.setInt(2, m.getModalidadeId());
            stmt.setString(3, m.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM Matriculas WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Matricula> listarTodos() {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.*, a.nome AS aluno_nome, mod.nome AS modalidade_nome " +
                     "FROM Matriculas m " +
                     "INNER JOIN Alunos a ON m.aluno_id = a.id " +
                     "INNER JOIN Modalidades mod ON m.modalidade_id = mod.id " +
                     "ORDER BY m.id DESC";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Matricula m = new Matricula(
                    rs.getInt("id"),
                    rs.getInt("aluno_id"),
                    rs.getInt("modalidade_id"),
                    rs.getString("data_matricula"),
                    rs.getString("status")
                );
                m.setNomeAluno(rs.getString("aluno_nome"));
                m.setNomeModalidade(rs.getString("modalidade_nome"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
