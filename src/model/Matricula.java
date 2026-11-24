package model;

public class Matricula {
    private int id;
    private int alunoId;
    private int modalidadeId;
    private String dataMatricula;
    private String status;

    // Campos auxiliares para exibição
    private String nomeAluno;
    private String nomeModalidade;

    public Matricula() {}

    public Matricula(int id, int alunoId, int modalidadeId, String dataMatricula, String status) {
        this.id = id;
        this.alunoId = alunoId;
        this.modalidadeId = modalidadeId;
        this.dataMatricula = dataMatricula;
        this.status = status;
    }

    public int getId() {
        return id;
        }
    public void setId(int id) {
        this.id = id;
        }

    public int getAlunoId() {
        return alunoId;
        }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
        }

    public int getModalidadeId() {
        return modalidadeId;
        }
    public void setModalidadeId(int modalidadeId) {
        this.modalidadeId = modalidadeId;
        }

    public String getDataMatricula() {
        return dataMatricula;
        }
        
    public void setDataMatricula(String dataMatricula) {
        this.dataMatricula = dataMatricula;
        }

    public String getStatus() { 
        return status; 
    }


    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getNomeAluno() { 
        return nomeAluno;
     }

    public void setNomeAluno(String nomeAluno) { 
        this.nomeAluno = nomeAluno;
     }

    public String getNomeModalidade() {
        return nomeModalidade;
     }
     
    public void setNomeModalidade(String nomeModalidade) { 
        this.nomeModalidade = nomeModalidade; 
    }
}
