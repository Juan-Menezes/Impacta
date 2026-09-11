package br.unifacisa.service;

import br.unifacisa.exception.EmailCadastradoException;
import br.unifacisa.model.Voluntario;

import java.util.*;

public class VoluntarioService {
    private HashMap<String, Voluntario> voluntarios = new HashMap<>();

    public boolean cadastrarVoluntario(String nome, String email) {
        if (voluntarios.containsKey(email)) {
            throw new EmailCadastradoException("E-mail já cadastrado.");
        }

        Voluntario novoVoluntario = new Voluntario(nome, email);
        voluntarios.put(email, novoVoluntario);
        return true;
    }

    public Voluntario buscarPorEmail(String email) {
        return voluntarios.get(email);
    }

    public String exibirVoluntario(String email) {
        Voluntario voluntario = buscarPorEmail(email);
        if (voluntario == null) {
            return null;
        }
        return voluntario.toString();
    }

    public String[] listarVoluntarios() {
        List<Voluntario> voluntariosOrdenados = new ArrayList<>(voluntarios.values());

        voluntariosOrdenados.sort(Comparator.comparingInt(Voluntario::getPontuacaoImpactoAcumulado)
            .reversed()
            .thenComparing(Voluntario::getNome));

        ArrayList<String> listaVoluntarios = new ArrayList<>();
        for (Voluntario vol : voluntariosOrdenados) {
            listaVoluntarios.add(vol.toString());
        }
        return listaVoluntarios.toArray(new String[0]);
    }

    public HashMap<String, Voluntario> getVoluntarios() {
        return voluntarios;
    }
}
