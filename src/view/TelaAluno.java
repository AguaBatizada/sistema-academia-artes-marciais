package view;

import dao.DAOException;
import controller.AlunoController;
import model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaAluno extends JFrame {
    private JTextField txtId, txtNome, txtEmail, txtTelefone, txtFaixa, txtCpf, txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private AlunoController controller;
    private JComboBox<String> cbOrdenacao;

    public TelaAluno() {
        controller = new AlunoController();

        setTitle("Gerenciamento de Alunos — Academia de Artes Marciais");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("CADASTRO E CONSULTA DE ALUNOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridLayout(0, 2, 5, 5));

        painelForm.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        painelForm.add(txtId);

        painelForm.add(new JLabel("Nome do Aluno:"));
        txtNome = new JTextField();
        painelForm.add(txtNome);

        painelForm.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelForm.add(txtEmail);

        painelForm.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelForm.add(txtTelefone);

        painelForm.add(new JLabel("Faixa (Ex: Branca, Azul, Preta):"));
        txtFaixa = new JTextField();
        painelForm.add(txtFaixa);

        painelForm.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painelForm.add(txtCpf);

        // Painel de Ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        txtBusca = new JTextField(12);
        cbOrdenacao = new JComboBox<>(new String[]{"ID (Padrão)", "Nome (A-Z)", "Faixa (A-Z)"});

        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnPesquisar = new JButton("Pesquisar");

        painelAcoes.add(new JLabel("Buscar Nome:"));
        painelAcoes.add(txtBusca);
        painelAcoes.add(new JLabel("Ordenar:"));
        painelAcoes.add(cbOrdenacao);
        painelAcoes.add(btnPesquisar);
        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        JPanel painelNorteCentro = new JPanel(new BorderLayout());
        painelNorteCentro.add(painelForm, BorderLayout.CENTER);
        painelNorteCentro.add(painelAcoes, BorderLayout.SOUTH);
        painelPrincipal.add(painelNorteCentro, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "E-mail", "Telefone", "Faixa", "CPF"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);

        add(painelPrincipal);

        // Eventos
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnPesquisar.addActionListener(e -> carregarTabela());

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    txtId.setText(tabela.getValueAt(linha, 0).toString());
                    txtNome.setText(tabela.getValueAt(linha, 1).toString());
                    txtEmail.setText(tabela.getValueAt(linha, 2).toString());
                    txtTelefone.setText(tabela.getValueAt(linha, 3).toString());
                    txtFaixa.setText(tabela.getValueAt(linha, 4).toString());
                    txtCpf.setText(tabela.getValueAt(linha, 5).toString());
                }
            }
        });

        carregarTabela();
        setVisible(true);
    }

    private void salvar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome do aluno é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Aluno a = new Aluno();
            if (!txtId.getText().isEmpty()) {
                a.setId(Integer.parseInt(txtId.getText()));
            }
            a.setNome(txtNome.getText());
            a.setEmail(txtEmail.getText());
            a.setTelefone(txtTelefone.getText());
            a.setFaixa(txtFaixa.getText());
            a.setCpf(txtCpf.getText());

            controller.salvar(a);
            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!");
            limpar();
            carregarTabela();
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este aluno?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(Integer.parseInt(txtId.getText()));
                JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
                limpar();
                carregarTabela();
            } catch (DAOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtFaixa.setText("");
        txtCpf.setText("");
        txtBusca.setText("");
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        String ordenacao = "id";
        int indexOrd = cbOrdenacao.getSelectedIndex();
        if (indexOrd == 1) ordenacao = "nome";
        else if (indexOrd == 2) ordenacao = "faixa";

        String filtro = txtBusca.getText();
        List<Aluno> lista;
        try {
            lista = controller.listar(ordenacao, filtro);
        } catch (DAOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Aluno a : lista) {
            modeloTabela.addRow(new Object[]{
                a.getId(), a.getNome(), a.getEmail(), a.getTelefone(), a.getFaixa(), a.getCpf()
            });
        }
    }
}
