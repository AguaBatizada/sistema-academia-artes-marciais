package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Gestão — Academia de Artes Marciais");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(15, 15));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("ACADEMIA DE ARTES MARCIAIS (MVC / DAO / SQL SERVER)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 15, 15));

        JButton btnAlunos = new JButton("Gerenciar Alunos");
        JButton btnModalidades = new JButton("Gerenciar Modalidades (Kung Fu, Jiu-Jitsu, etc.)");
        JButton btnMatriculas = new JButton("Gerenciar Matrículas (Associação)");

        btnAlunos.setFont(new Font("Arial", Font.PLAIN, 14));
        btnModalidades.setFont(new Font("Arial", Font.PLAIN, 14));
        btnMatriculas.setFont(new Font("Arial", Font.PLAIN, 14));

        painelBotoes.add(btnAlunos);
        painelBotoes.add(btnModalidades);
        painelBotoes.add(btnMatriculas);

        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        JLabel lblRodape = new JLabel("Sistema integrado para APS e Trabalho de Banco de Dados — UNIP", SwingConstants.CENTER);
        lblRodape.setFont(new Font("Arial", Font.ITALIC, 11));
        painelPrincipal.add(lblRodape, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Ações
        btnAlunos.addActionListener(e -> new TelaAluno());
        btnModalidades.addActionListener(e -> new TelaModalidade());
        btnMatriculas.addActionListener(e -> new TelaMatricula());

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}
