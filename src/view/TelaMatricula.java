package view;

import dao.DAOException;
import controller.AlunoController;
import controller.ModalidadeController;
import controller.MatriculaController;
import model.Aluno;
import model.Modalidade;
import model.Matricula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaMatricula extends JFrame {
    private JComboBox<Aluno> cbAlunos;
    private JComboBox<Modalidade> cbModalidades;
    private JTextField txtIdMatricula;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private MatriculaController matriculaController;
    private AlunoController alunoController;
    private ModalidadeController modalidadeController;

    public TelaMatricula() {
        matriculaController = new MatriculaController();
        alunoController = new AlunoController();
        modalidadeController = new ModalidadeController();

        setTitle("Registro e Consulta de Matrículas (Tabela Associativa)");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("REGISTRO DE MATRÍCULAS (RELAÇÃO ALUNO <-> MODALIDADE)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 5, 5));

        painelForm.add(new JLabel("ID Matrícula:"));
        txtIdMatricula = new JTextField();
        txtIdMatricula.setEditable(false);
        painelForm.add(txtIdMatricula);

        painelForm.add(new JLabel("Aluno:"));
        cbAlunos = new JComboBox<>();
        painelForm.add(cbAlunos);

        painelForm.add(new JLabel("Modalidade (Kung Fu, Jiu-Jitsu, etc.):"));
        cbModalidades = new JComboBox<>();
        painelForm.add(cbModalidades);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnMatricular = new JButton("Realizar Matrícula");
        JButton btnExcluir = new JButton("Cancelar Matrícula");
        JButton btnAtualizar = new JButton("Atualizar Listas");
        painelBotoes.add(btnMatricular);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);

        JPanel painelNorte = new JPanel(new BorderLayout());
        painelNorte.add(painelForm, BorderLayout.CENTER);
        painelNorte.add(painelBotoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelNorte, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID Matrícula", "Aluno", "Modalidade", "Status", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);

        add(painelPrincipal);

        // Eventos
        btnMatricular.addActionListener(e -> realizarMatricula());
        btnExcluir.addActionListener(e -> excluirMatricula());
        btnAtualizar.addActionListener(e -> carregarCombos());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    txtIdMatricula.setText(tabela.getValueAt(linha, 0).toString());
                }
            }
        });

        carregarCombos();
        carregarTabela();
        setVisible(true);
    }

    private void carregarCombos() {
        try {
            preencherCombos();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCombos() {
        cbAlunos.removeAllItems();
        List<Aluno> alunos = alunoController.listar("id", "");
        for (Aluno a : alunos) {
            cbAlunos.addItem(a);
        }

        cbModalidades.removeAllItems();
        List<Modalidade> modalidades = modalidadeController.listar("id", "");
        for (Modalidade m : modalidades) {
            cbModalidades.addItem(m);
        }
    }

    private void realizarMatricula() {
        try {
            Aluno alunoSelecionado = (Aluno) cbAlunos.getSelectedItem();
            Modalidade modalidadeSelecionada = (Modalidade) cbModalidades.getSelectedItem();

            if (alunoSelecionado == null || modalidadeSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno e uma modalidade válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Matricula m = new Matricula();
            m.setAlunoId(alunoSelecionado.getId());
            m.setModalidadeId(modalidadeSelecionada.getId());
            m.setStatus("Ativo");

            matriculaController.salvar(m);

            JOptionPane.showMessageDialog(this, "Matrícula realizada com sucesso na modalidade " + modalidadeSelecionada.getNome() + "!");
            txtIdMatricula.setText("");
            carregarTabela();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar matrícula.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirMatricula() {
        if (txtIdMatricula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma matrícula na tabela para cancelar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente cancelar esta matrícula?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                matriculaController.excluir(Integer.parseInt(txtIdMatricula.getText()));
                JOptionPane.showMessageDialog(this, "Matrícula cancelada com sucesso!");
                txtIdMatricula.setText("");
                carregarTabela();
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Matricula> lista;
        try {
            lista = matriculaController.listar();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Matricula m : lista) {
            modeloTabela.addRow(new Object[]{
                m.getId(), m.getNomeAluno(), m.getNomeModalidade(), m.getStatus(), m.getDataMatricula()
            });
        }
    }
}
