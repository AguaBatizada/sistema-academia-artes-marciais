package controller;

import dao.MatriculaDAO;
import model.Matricula;
import java.util.List;

public class MatriculaController {
    private MatriculaDAO matriculaDAO;

    public MatriculaController() {
        this.matriculaDAO = new MatriculaDAO();
    }

    public void salvar(Matricula m) {
        matriculaDAO.inserir(m);
    }

    public void excluir(int id) {
        matriculaDAO.excluir(id);
    }

    public List<Matricula> listar() {
        return matriculaDAO.listarTodos();
    }
}
