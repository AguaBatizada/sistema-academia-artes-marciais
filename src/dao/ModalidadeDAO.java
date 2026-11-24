package dao;

import conexao.ConexaoBanco;
import model.Modalidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModalidadeDAO {

    public void inserir(Modalidade m) {
        String sql = "INSERT INTO Modalidades (nome, professor, mensalidade, horario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getProfessor());
            stmt.setDouble(3, m.getMensalidade());
            stmt.setString(4, m.getHorario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Modalidade m) {
        String sql = "UPDATE Modalidades SET nome = ?, professor = ?, mensalidade = ?, horario = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getNome());
            stmt.setString(2, m.getProfessor());
            stmt.setDouble(3, m.getMensalidade());
            stmt.setString(4, m.getHorario());
            stmt.setInt(5, m.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM Modalidades WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Modalidade> listarTodos(String ordenacao, String filtroNome) {
        List<Modalidade> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Modalidades WHERE 1=1");

        if (filtroNome != null && !filtroNome.trim().isEmpty()) {
            sql.append(" AND nome LIKE ?");
        }

        if ("nome".equals(ordenacao)) {
            sql.append(" ORDER BY nome ASC");
        } else if ("mensalidade".equals(ordenacao)) {
            sql.append(" ORDER BY mensalidade DESC");
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
                    Modalidade m = new Modalidade(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("professor"),
                        rs.getDouble("mensalidade"),
                        rs.getString("horario")
                    );
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
