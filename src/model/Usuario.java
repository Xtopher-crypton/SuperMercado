package model;

public class Usuario {
    private String nome;
    private String cpf;
    private boolean eAdministrador;

    public Usuario(String nome, String cpf, boolean eAdministrador) {
        this.nome = nome;
        this.cpf = cpf;
        this.eAdministrador = eAdministrador;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean eAdministrador() {
        return eAdministrador;
    }
}
