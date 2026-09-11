package br.unifacisa.model;

import br.unifacisa.exception.AcaoLotadaException;

import java.time.LocalDateTime;
import java.util.HashMap;

public abstract class Acao {
    private String titulo;
    private String descricao;
    private LocalDateTime data;
    private int maxCapacidade;
    private HashMap<String, Voluntario> voluntariosInscritos = new HashMap<>();
    private int idAcao;
    private static int quantidadeAcoes = 0;

    public Acao(String titulo, String descricao, LocalDateTime data, int maxCapacidade) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Titulo nao pode ser vazio.");
        }
        if (maxCapacidade <= 0) {
            throw new IllegalArgumentException("Capacidade maxima invalida.");
        }

        this.titulo = titulo;
        this.descricao = descricao;
        this.data = (data != null) ? data : LocalDateTime.now();
        this.maxCapacidade = maxCapacidade;
        this.idAcao = ++quantidadeAcoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public int getMaxCapacidade() {
        return maxCapacidade;
    }

    public void setMaxCapacidade(int maxCapacidade) {
        this.maxCapacidade = maxCapacidade;
    }

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public boolean inscreverVoluntario(Voluntario voluntario) {
        if (voluntario == null)  { return false; }
        if (voluntariosInscritos.containsKey(voluntario.getEmail())) { return false; }
        if (isLotada()) {
            throw new AcaoLotadaException("Acao lotada.");
        };

        voluntariosInscritos.put(voluntario.getEmail(), voluntario);
        return true;
    }

    public boolean isLotada() {
        return voluntariosInscritos.size() >= maxCapacidade;
    }

    public int getQuantidadeInscritos() {
        return voluntariosInscritos.size();
    }

    public HashMap<String, Voluntario> getVoluntariosInscritos() {
        return voluntariosInscritos;
    }

    public static int getQuantidadeAcoes() {
        return quantidadeAcoes;
    }

    public static void resetarContador() {
        quantidadeAcoes = 0;
    }

    @Override
    public String toString() {
        return "\nACAO\n" +
            "ID: " + idAcao + '\n' +
            "Titulo: " + titulo + '\n' +
            "Descricao: " + descricao + '\n' +
            "Data: " + data + '\n' +
            "Capacidade maxima: " + maxCapacidade + '\n' +
            "Inscritos: " + getQuantidadeInscritos() + "/" + maxCapacidade + '\n' +
            "Pontuacao: " + calcularPontuacao() + " pontos";
    }

    public abstract int calcularPontuacao();
}
