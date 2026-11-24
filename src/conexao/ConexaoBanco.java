package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public static Connection getConexao() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=academia_db;encrypt=true;trustServerCertificate=true;";
        // Credenciais lidas de variáveis de ambiente (veja o README): DB_USUARIO e DB_SENHA
        String usuario = System.getenv().getOrDefault("DB_USUARIO", "sa");
        String senha = System.getenv("DB_SENHA");

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco.");
            e.printStackTrace();
            return null;
        }
    }
}