package br.unifacisa.model;

import java.time.LocalDateTime;

public class MutiraoDeReciclagem extends Acao {

    private int duracaoHoras;

    public MutiraoDeReciclagem(String titulo, String descricao, LocalDateTime data, int maxCapacidade, int duracaoHoras) {
        super(titulo, descricao, data, maxCapacidade);
        if (duracaoHoras <= 0) {
            throw new IllegalArgumentException("Duracao invalida.");
        }
        this.duracaoHoras = duracaoHoras;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

    public void setDuracaoHoras(int duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }

    @Override
    public int calcularPontuacao() {
        return 4 * duracaoHoras;
    }
}
