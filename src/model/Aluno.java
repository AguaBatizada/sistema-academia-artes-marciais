package model;

public class Aluno {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String faixa;
    private String cpf;

    public Aluno() {}

    public Aluno(int id, String nome, String email, String telefone, String faixa, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.faixa = faixa;
        this.cpf = cpf;
    }

    public int getId() {
         return id;
        }

    public void setId(int id) {
         this.id = id;
        }

    public String getNome() {
        return nome;
        }

    public void setNome(String nome) {
        this.nome = nome;
        }

    public String getEmail() {
        return email;
        }

    public void setEmail(String email) {
        this.email = email;
        }

    public String getTelefone() {
         return telefone;
        }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
        }

    public String getFaixa() {
         return faixa;
        }

    public void setFaixa(String faixa) {
        this.faixa = faixa;
        }

    public String getCpf() {
         return cpf;
        }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        }

    @Override
    public String toString() {
        return nome + " (Faixa: " + faixa + ")";
    }

}
