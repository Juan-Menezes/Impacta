package br.unifacisa.model;

import java.time.LocalDateTime;

public class PlantioDeMudas extends Acao {

    private int quantidadeMudas;

    public PlantioDeMudas(String titulo, String descricao, LocalDateTime data, int maxCapacidade, int quantidadeMudas) {
        super(titulo, descricao, data, maxCapacidade);
        if (quantidadeMudas <= 0) {
            throw new IllegalArgumentException("Quantidade de mudas invalida.");
        }
        this.quantidadeMudas = quantidadeMudas;
    }

    public int getQuantidadeMudas() {
        return quantidadeMudas;
    }

    public void setQuantidadeMudas(int quantidadeMudas) {
        this.quantidadeMudas = quantidadeMudas;
    }

    @Override
    public int calcularPontuacao() {
        return 5 + ( 2 * quantidadeMudas);
    }
}
