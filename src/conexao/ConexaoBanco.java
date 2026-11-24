package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=academia_db;encrypt=true;trustServerCertificate=true;";

    /**
     * Abre uma conexão com o SQL Server.
     * Credenciais lidas de variáveis de ambiente (veja o README): DB_USUARIO e DB_SENHA.
     *
     * @throws SQLException se a senha não estiver configurada ou a conexão falhar
     */
    public static Connection getConexao() throws SQLException {
        String usuario = System.getenv().getOrDefault("DB_USUARIO", "sa");
        String senha = System.getenv("DB_SENHA");

        if (senha == null || senha.isEmpty()) {
            throw new SQLException("Variável de ambiente DB_SENHA não definida.");
        }
        return DriverManager.getConnection(URL, usuario, senha);
    }
}
