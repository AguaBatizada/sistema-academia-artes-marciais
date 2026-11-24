package controller;

import dao.AlunoDAO;
import model.Aluno;
import java.util.List;

public class AlunoController {
    private AlunoDAO alunoDAO;

    public AlunoController() {
        this.alunoDAO = new AlunoDAO();
    }

    public void salvar(Aluno a) {
        if (a.getId() == 0) {
            alunoDAO.inserir(a);
        } else {
            alunoDAO.atualizar(a);
        }
    }

    public void excluir(int id) {
        alunoDAO.excluir(id);
    }

    public List<Aluno> listar(String ordenacao, String filtroNome) {
        return alunoDAO.listarTodos(ordenacao, filtroNome);
    }
}
