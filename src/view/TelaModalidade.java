package view;

import controller.ModalidadeController;
import model.Modalidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaModalidade extends JFrame {
    private JTextField txtId, txtNome, txtProfessor, txtMensalidade, txtHorario, txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private ModalidadeController controller;
    private JComboBox<String> cbOrdenacao;

    public TelaModalidade() {
        controller = new ModalidadeController();

        setTitle("Gerenciamento de Modalidades (Kung Fu, Jiu-Jitsu, Muay Thai, etc.)");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("CADASTRO E CONSULTA DE MODALIDADES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridLayout(5, 2, 5, 5));

        painelForm.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        painelForm.add(txtId);

        painelForm.add(new JLabel("Nome da Modalidade:"));
        txtNome = new JTextField();
        painelForm.add(txtNome);

        painelForm.add(new JLabel("Professor:"));
        txtProfessor = new JTextField();
        painelForm.add(txtProfessor);

        painelForm.add(new JLabel("Mensalidade (R$):"));
        txtMensalidade = new JTextField();
        painelForm.add(txtMensalidade);

        painelForm.add(new JLabel("Horário:"));
        txtHorario = new JTextField();
        painelForm.add(txtHorario);

        // Painel de Ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        txtBusca = new JTextField(12);
        cbOrdenacao = new JComboBox<>(new String[]{"ID (Padrão)", "Nome (A-Z)", "Mensalidade (Maior)"});

        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnPesquisar = new JButton("Pesquisar");

        painelAcoes.add(new JLabel("Buscar Modalidade:"));
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
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Modalidade", "Professor", "Mensalidade (R$)", "Horário"}, 0) {
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
                    txtProfessor.setText(tabela.getValueAt(linha, 2).toString());
                    txtMensalidade.setText(tabela.getValueAt(linha, 3).toString());
                    txtHorario.setText(tabela.getValueAt(linha, 4).toString());
                }
            }
        });

        carregarTabela();
        setVisible(true);
    }

    private void salvar() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome da modalidade é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Modalidade m = new Modalidade();
            if (!txtId.getText().isEmpty()) {
                m.setId(Integer.parseInt(txtId.getText()));
            }
            m.setNome(txtNome.getText());
            m.setProfessor(txtProfessor.getText());
            m.setMensalidade(Double.parseDouble(txtMensalidade.getText()));
            m.setHorario(txtHorario.getText());

            controller.salvar(m);
            JOptionPane.showMessageDialog(this, "Modalidade salva com sucesso!");
            limpar();
            carregarTabela();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique o campo numérico (Mensalidade).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma modalidade na tabela para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir esta modalidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.excluir(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Modalidade excluída com sucesso!");
            limpar();
            carregarTabela();
        }
    }

    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtProfessor.setText("");
        txtMensalidade.setText("");
        txtHorario.setText("");
        txtBusca.setText("");
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        String ordenacao = "id";
        int indexOrd = cbOrdenacao.getSelectedIndex();
        if (indexOrd == 1) ordenacao = "nome";
        else if (indexOrd == 2) ordenacao = "mensalidade";

        String filtro = txtBusca.getText();
        List<Modalidade> lista = controller.listar(ordenacao, filtro);

        for (Modalidade m : lista) {
            modeloTabela.addRow(new Object[]{
                m.getId(), m.getNome(), m.getProfessor(), m.getMensalidade(), m.getHorario()
            });
        }
    }
}
