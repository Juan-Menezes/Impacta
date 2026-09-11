package br.unifacisa.model;

import br.unifacisa.exception.EmailInvalidoException;

public class Voluntario {
    private String nome;
    private String email;
    private String matricula;
    private int quantidadeAcoes;
    private int pontuacaoImpactoAcumulado;
    private static int quantidadeVoluntarios = 0;

    public Voluntario(String nome, String email) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome nao pode ser vazio.");
        }
        if (email == null || !email.contains("@")) {
            throw new EmailInvalidoException("E-mail invalido. Tente novamente.");
        }
        this.nome = nome;
        this.email = email;
        this.matricula = "2026" + String.format("%03d", ++quantidadeVoluntarios);
        this.quantidadeAcoes = 0;
        this.pontuacaoImpactoAcumulado = 0;
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

    public String getMatricula() {
        return matricula;
    }

    public int getQuantidadeAcoes() {
        return quantidadeAcoes;
    }

    public void setQuantidadeAcoes(int quantidadeAcoes) {
        this.quantidadeAcoes = quantidadeAcoes;
    }

    public int getPontuacaoImpactoAcumulado() {
        return pontuacaoImpactoAcumulado;
    }

    public void setPontuacaoImpactoAcumulado(int pontuacaoImpactoAcumulado) {
        this.pontuacaoImpactoAcumulado = pontuacaoImpactoAcumulado;
    }

    public static int getQuantidadeVoluntarios() {
        return quantidadeVoluntarios;
    }

    public void registrarParticipacao(int pontosGanhos) {
        this.quantidadeAcoes++;
        this.pontuacaoImpactoAcumulado += pontosGanhos;
    }

    public static void resetarContador() {
        quantidadeVoluntarios = 0;
    }

    @Override
    public String toString() {
        return "\nVOLUNTARIO \n" +
            "Nome: " + nome + '\n' +
            "Email: " + email + '\n' +
            "Matricula: " + matricula + '\n' +
            "Quantidade de ações: " + quantidadeAcoes + '\n' +
            "Pontuacao do impacto acumulado: " + pontuacaoImpactoAcumulado + " pontos";
    }
}
