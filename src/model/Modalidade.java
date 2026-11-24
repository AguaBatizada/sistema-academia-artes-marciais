package model;

public class Modalidade {
    private int id;
    private String nome;
    private String professor;
    private double mensalidade;
    private String horario;

    public Modalidade() {}

    public Modalidade(int id, String nome, String professor, double mensalidade, String horario) {
        this.id = id;
        this.nome = nome;
        this.professor = professor;
        this.mensalidade = mensalidade;
        this.horario = horario;
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

    public String getProfessor() {
        return professor; 
    }
    public void setProfessor(String professor) {
        this.professor = professor; 
    }

    public double getMensalidade() {
        return mensalidade; 
    }
    public void setMensalidade(double mensalidade) {
        this.mensalidade = mensalidade; 
    }

    public String getHorario() {
        return horario; 
    }
    public void setHorario(String horario) {
        this.horario = horario; 
    }

    @Override
    public String toString() {
        return nome + " - Prof. " + professor + " (R$ " + mensalidade + ")";
    }
}
