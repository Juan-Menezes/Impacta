package br.unifacisa.model;

import java.time.LocalDateTime;

public class OficinaEcologica extends Acao {

    private int duracaoHoras;
    private boolean kitMaterial;

    public OficinaEcologica(String titulo, String descricao, LocalDateTime data, int maxCapacidade, int duracaoHoras, boolean kitMaterial) {
        super(titulo, descricao, data, maxCapacidade);
        if (duracaoHoras <= 0) {
            throw new IllegalArgumentException("Duracao invalida.");
        }
        this.duracaoHoras = duracaoHoras;
        this.kitMaterial = kitMaterial;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

    public void setDuracaoHoras(int duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }

    public boolean isKitMaterial() {
        return kitMaterial;
    }

    public void setKitMaterial(boolean kitMaterial) {
        this.kitMaterial = kitMaterial;
    }

    @Override
    public int calcularPontuacao() {
        return 3 * duracaoHoras + (kitMaterial ? 10 : 0);
    }
}
