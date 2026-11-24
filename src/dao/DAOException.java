package dao;

/**
 * Erro de acesso ao banco de dados, lançado pelos DAOs para que a camada
 * de interface possa informar o usuário em vez de exibir uma falsa mensagem de sucesso.
 */
public class DAOException extends RuntimeException {
    public DAOException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
