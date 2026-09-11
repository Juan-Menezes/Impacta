package br.unifacisa.test;

import br.unifacisa.exception.AcaoLotadaException;
import br.unifacisa.model.Acao;
import br.unifacisa.model.MutiraoDeReciclagem;
import br.unifacisa.model.OficinaEcologica;
import br.unifacisa.model.PlantioDeMudas;
import br.unifacisa.model.Voluntario;
import br.unifacisa.service.AcaoService;
import br.unifacisa.service.VoluntarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AcaoTest {

    private AcaoService acaoService;
    private VoluntarioService voluntarioService;

    @BeforeEach
    public void setUp() {
        Acao.resetarContador();
        Voluntario.resetarContador();
        voluntarioService = new VoluntarioService();
        acaoService = new AcaoService(voluntarioService);
    }

    @Test
    @DisplayName("Deve cadastrar um plantio de mudas")
    public void deveCadastrarPlantio() {
        int idAcao = acaoService.cadastrarPlantio("Plantio no Parque", "Plantio de mudas nativas", LocalDateTime.now(), 20, 50);

        assertTrue(idAcao > 0);
        assertEquals(1, acaoService.getAcoes().size());
        assertTrue(acaoService.getAcoes().get(idAcao) instanceof PlantioDeMudas);
    }

    @Test
    @DisplayName("Deve cadastrar um mutirao de reciclagem")
    public void deveCadastrarMutirao() {
        int idAcao = acaoService.cadastrarMutirao("Mutirao de Reciclagem", "Coleta e separacao de residuos", LocalDateTime.now(), 30, 4);

        assertTrue(idAcao > 0);
        assertEquals(1, acaoService.getAcoes().size());
        assertTrue(acaoService.getAcoes().get(idAcao) instanceof MutiraoDeReciclagem);
    }

    @Test
    @DisplayName("Deve cadastrar uma oficina ecologica")
    public void deveCadastrarOficina() {
        int idAcao = acaoService.cadastrarOficina("Oficina de Compostagem", "Aprenda a compostar em casa", LocalDateTime.now(), 15, 2, true);

        assertTrue(idAcao > 0);
        assertEquals(1, acaoService.getAcoes().size());
        assertTrue(acaoService.getAcoes().get(idAcao) instanceof OficinaEcologica);
    }

    @Test
    @DisplayName("Nao deve cadastrar acao com titulo vazio")
    public void naoDeveCadastrarAcaoComTituloVazio() {
        assertThrows(IllegalArgumentException.class, () ->
            acaoService.cadastrarPlantio("", "Descricao", LocalDateTime.now(), 10, 5));
    }

    @Test
    @DisplayName("Nao deve cadastrar acao com capacidade invalida")
    public void naoDeveCadastrarAcaoComCapacidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
            acaoService.cadastrarMutirao("Mutirao", "Descricao", LocalDateTime.now(), 0, 4));
    }

    @Test
    @DisplayName("Testa se os ID's são únicos")
    public void idsDevemSerUnicos() {
        int id1 = acaoService.cadastrarPlantio("Plantio 1", "Desc", LocalDateTime.now(), 10, 5);
        int id2 = acaoService.cadastrarMutirao("Mutirao 1", "Desc", LocalDateTime.now(), 10, 3);

        assertNotEquals(id1, id2);
        assertEquals(2, acaoService.getAcoes().size());
    }

    @Test
    @DisplayName("Deve inscrever voluntario em uma acao existente")
    public void deveInscreverVoluntarioEmAcaoExistente() {
        voluntarioService.cadastrarVoluntario("Juan Menezes", "juan@gmail.com");
        int idAcao = acaoService.cadastrarPlantio("Plantio no Parque", "Plantio de mudas nativas", LocalDateTime.now(), 20, 50);

        boolean resultado = acaoService.inscreverVoluntario("juan@gmail.com", idAcao);

        assertTrue(resultado);
        assertEquals(1, acaoService.getAcoes().get(idAcao).getQuantidadeInscritos());
    }

    @Test
    @DisplayName("Testa se voluntario ganha pontuacao")
    public void voluntarioDeveGanharPontuacaoAoInscrever() {
        voluntarioService.cadastrarVoluntario("Juan Menezes", "juan@gmail.com");
        int idAcao = acaoService.cadastrarMutirao("Mutirao de Reciclagem", "Desc", LocalDateTime.now(), 10, 5);

        acaoService.inscreverVoluntario("juan@gmail.com", idAcao);

        Voluntario voluntario = voluntarioService.buscarPorEmail("juan@gmail.com");
        assertEquals(1, voluntario.getQuantidadeAcoes());
        assertEquals(4 * 5, voluntario.getPontuacaoImpactoAcumulado());
    }

    @Test
    @DisplayName("Nao deve inscrever em acao inexistente")
    public void naoDeveInscreverVoluntarioEmAcaoInexistente() {
        voluntarioService.cadastrarVoluntario("Juan Menezes", "juan@gmail.com");
        assertFalse(acaoService.inscreverVoluntario("juan@gmail.com", 999));
    }

    @Test
    @DisplayName("Nao deve inscrever voluntario inexistente")
    public void naoDeveInscreverVoluntarioNaoCadastrado() {
        int idAcao = acaoService.cadastrarPlantio("Plantio no Parque", "Desc", LocalDateTime.now(), 20, 50);
        assertFalse(acaoService.inscreverVoluntario("naoexiste@gmail.com", idAcao));
    }

    @Test
    @DisplayName("Nao deve inscrever voluntario duplicado")
    public void naoDeveInscreverVoluntarioDuplicado() {
        voluntarioService.cadastrarVoluntario("Juan Menezes", "juan@gmail.com");
        int idAcao = acaoService.cadastrarPlantio("Plantio no Parque", "Desc", LocalDateTime.now(), 20, 50);

        acaoService.inscreverVoluntario("juan@gmail.com", idAcao);
        boolean resultado = acaoService.inscreverVoluntario("juan@gmail.com", idAcao);

        assertFalse(resultado);
        assertEquals(1, acaoService.getAcoes().get(idAcao).getQuantidadeInscritos());
    }

    @Test
    @DisplayName("Nao deve inscrever em acao lotada")
    public void naoDeveInscreverVoluntarioQuandoLotada() {
        voluntarioService.cadastrarVoluntario("Voluntario 1", "v1@gmail.com");
        voluntarioService.cadastrarVoluntario("Voluntario 2", "v2@gmail.com");
        int idAcao = acaoService.cadastrarPlantio("Plantio no Parque", "Desc", LocalDateTime.now(), 1, 50);

        boolean primeira = acaoService.inscreverVoluntario("v1@gmail.com", idAcao);
        assertThrows(AcaoLotadaException.class, () -> {
            acaoService.inscreverVoluntario("v2@gmail.com", idAcao);
        });

        assertTrue(primeira);
        assertEquals(1, acaoService.getAcoes().get(idAcao).getQuantidadeInscritos());
    }

    @Test
    @DisplayName("Testa exibicao de detalhes")
    public void deveExibirDetalhesDeAcaoExistente() {
        int idAcao = acaoService.cadastrarMutirao("Mutirao de Reciclagem", "Coleta e separacao de residuos", LocalDateTime.now(), 30, 4);
        String detalhes = acaoService.exibirDetalhesAcao(idAcao);

        assertNotNull(detalhes);
        assertTrue(detalhes.contains("Mutirao de Reciclagem"));
    }

    @Test
    public void deveRetornarNullParaAcaoInexistente() {
        assertNull(acaoService.exibirDetalhesAcao(999));
    }

    @Test
    public void devecalcularPontuacaoPlantio() {
        int idAcao = acaoService.cadastrarPlantio("Plantio", "Desc", LocalDateTime.now(), 10, 20);
        Acao acao = acaoService.getAcoes().get(idAcao);

        assertEquals(5 + (2 * 20), acao.calcularPontuacao());
    }

    @Test
    public void devecalcularPontuacaoMutirao() {
        int idAcao = acaoService.cadastrarMutirao("Mutirao", "Desc", LocalDateTime.now(), 10, 6);
        Acao acao = acaoService.getAcoes().get(idAcao);

        assertEquals(4 * 6, acao.calcularPontuacao());
    }

    @Test
    public void devecalcularPontuacaoOficina() {
        int idComKit = acaoService.cadastrarOficina("Oficina 1", "Desc", LocalDateTime.now(), 10, 3, true);
        int idSemKit = acaoService.cadastrarOficina("Oficina 2", "Desc", LocalDateTime.now(), 10, 3, false);

        Acao oficinaComKit = acaoService.getAcoes().get(idComKit);
        Acao oficinaSemKit = acaoService.getAcoes().get(idSemKit);

        assertEquals(3 * 3 + 10, oficinaComKit.calcularPontuacao());
        assertEquals(3 * 3, oficinaSemKit.calcularPontuacao());
    }
}
