package controller;

import dao.ModalidadeDAO;
import model.Modalidade;
import java.util.List;

public class ModalidadeController {
    private ModalidadeDAO modalidadeDAO;

    public ModalidadeController() {
        this.modalidadeDAO = new ModalidadeDAO();
    }

    public void salvar(Modalidade m) {
        if (m.getId() == 0) {
            modalidadeDAO.inserir(m);
        } else {
            modalidadeDAO.atualizar(m);
        }
    }

    public void excluir(int id) {
        modalidadeDAO.excluir(id);
    }

    public List<Modalidade> listar(String ordenacao, String filtroNome) {
        return modalidadeDAO.listarTodos(ordenacao, filtroNome);
    }
}
