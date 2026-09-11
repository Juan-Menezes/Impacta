package br.unifacisa.test;

import br.unifacisa.exception.EmailCadastradoException;
import br.unifacisa.exception.EmailInvalidoException;
import br.unifacisa.model.Voluntario;
import br.unifacisa.service.VoluntarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VoluntarioTest {

    private VoluntarioService voluntarioService;

    @BeforeEach
    public void setUp() {
//        Voluntario.resetarContador();
        voluntarioService = new VoluntarioService();
    }

    @Test
    @DisplayName("Deve cadastrar voluntario")
    public void deveAdicionarVoluntario() {
        boolean resultado = voluntarioService.cadastrarVoluntario("Juan Menezes","juan@gmail.com");
        assertTrue(resultado);
        assertEquals(1, voluntarioService.getVoluntarios().size());
    }

    @Test
    @DisplayName("Deve lancar EmailInvalidoException")
    public void deveLancarEmailInvalido() {
        assertThrows(EmailInvalidoException.class, () -> {
            voluntarioService.cadastrarVoluntario("Juan Menezes", "juansemgmail.com");
        });
        assertTrue(voluntarioService.getVoluntarios().isEmpty());
    }

    @Test
    @DisplayName("DeveLancarNomeVazio")
    public void deveLancarNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            voluntarioService.cadastrarVoluntario("   ", "juan@gmail.com");
        });
        assertTrue(voluntarioService.getVoluntarios().isEmpty());
    }

    @Test
    @DisplayName("Deve lancar EmailCadastradoException")
    public void deveLancarEmailCadastrado() {
        boolean resultado = voluntarioService.cadastrarVoluntario("Juan Menezes","juan@gmail.com");
        assertTrue(resultado);

        assertThrows(EmailCadastradoException.class, () -> {
            voluntarioService.cadastrarVoluntario("Juan Menezes 2", "juan@gmail.com");
        });
        assertEquals(1, voluntarioService.getVoluntarios().size());
    }

    @Test
    public void deveExibirVoluntario() {
        voluntarioService.cadastrarVoluntario("Juan Menezes", "juan@gmail.com");
        String detalhes = voluntarioService.exibirVoluntario("juan@gmail.com");
        assertNotNull(detalhes);
        assertTrue(detalhes.contains("Juan Menezes"));
    }

    @Test
    public void deveRetornarNullParaVoluntarioInexistente() {
        assertNull(voluntarioService.exibirVoluntario("naoexiste@gmail.com"));
    }

    @Test
    public void deveRetornarListaVaziaSemVoluntarios() {
        assertEquals(0, voluntarioService.listarVoluntarios().length);
    }

    @Test
    @DisplayName("Testa ordenação por pontuação")
    public void testeOrdenacaoPontuacao() {
        voluntarioService.cadastrarVoluntario("Bruno", "bruno@gmail.com");
        voluntarioService.cadastrarVoluntario("Ana", "ana@gmail.com");
        voluntarioService.cadastrarVoluntario("Carlos", "carlos@gmail.com");

        voluntarioService.getVoluntarios().get("bruno@gmail.com").registrarParticipacao(10);
        voluntarioService.getVoluntarios().get("ana@gmail.com").registrarParticipacao(30);
        voluntarioService.getVoluntarios().get("carlos@gmail.com").registrarParticipacao(30);

        String[] lista = voluntarioService.listarVoluntarios();

        assertEquals(3, lista.length);
        assertTrue(lista[0].contains("Ana"));
        assertTrue(lista[1].contains("Carlos"));
        assertTrue(lista[2].contains("Bruno"));
    }
}
