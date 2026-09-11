package br.unifacisa.service;
import br.unifacisa.model.Acao;
import br.unifacisa.model.MutiraoDeReciclagem;
import br.unifacisa.model.OficinaEcologica;
import br.unifacisa.model.PlantioDeMudas;
import br.unifacisa.model.Voluntario;

import java.time.LocalDateTime;
import java.util.HashMap;

public class AcaoService {

    private HashMap<Integer, Acao> acoes = new HashMap<>();
    private final VoluntarioService voluntarioService;

    public AcaoService(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    public int cadastrarPlantio(String titulo, String descricao,
                                LocalDateTime data, int maxParticipantes,
                                int qtdMudas) {

        return registrarAcao(new PlantioDeMudas(titulo, descricao, data, maxParticipantes, qtdMudas));
    }

    public int cadastrarMutirao(String titulo, String descricao,
                                LocalDateTime data, int maxParticipantes,
                                int duracaoHoras) {

        return registrarAcao(new MutiraoDeReciclagem(titulo, descricao, data, maxParticipantes, duracaoHoras));
    }

    public int cadastrarOficina(String titulo, String descricao,
                                LocalDateTime data, int maxParticipantes,
                                int duracaoHoras, boolean kitMaterial) {

        return registrarAcao(new OficinaEcologica(titulo, descricao, data, maxParticipantes, duracaoHoras, kitMaterial));
    }

    private int registrarAcao(Acao acao) {
        acoes.put(acao.getIdAcao(), acao);
        return acao.getIdAcao();
    }

    public boolean inscreverVoluntario(String emailVoluntario, int idAcao) {

        Acao acao = acoes.get(idAcao);
        if (acao == null) {
            return false;
        }

        Voluntario voluntario = voluntarioService.buscarPorEmail(emailVoluntario);
        if (voluntario == null) {
            return false;
        }

        boolean inscrito = acao.inscreverVoluntario(voluntario);
        if (!inscrito) {
            return false;
        }

        voluntario.registrarParticipacao(acao.calcularPontuacao());
        return true;
    }

    public String exibirDetalhesAcao(int idAcao) {

        Acao acao = acoes.get(idAcao);

        if (acao == null) {
            return null;
        }

        return acao.toString();
    }

    public HashMap<Integer, Acao> getAcoes() {
        return acoes;
    }
}
