package model.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.domain.Usuario;

public class GerenciadorDados {
    private final List<String> artistas;
    private final List<Usuario> usuarios = new CopyOnWriteArrayList<>();

    public GerenciadorDados() {
        // Apenas os 15 artistas ficam fixos
        artistas = List.of(
            "The Beatles", "Queen", "Pink Floyd", "Led Zeppelin",
            "Nirvana", "Metallica", "Iron Maiden", "U2",
            "Coldplay", "Imagine Dragons", "Legião Urbana", "Titãs",
            "Charlie Brown Jr.", "Skank", "Capital Inicial"
        );
        // Nenhum usuário pré-cadastrado
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public List<String> getNomesUsuarios() {
        return usuarios.stream().map(Usuario::getNome).toList();
    }

    /**
     * Adiciona (ou substitui) um usuário. Sincronizado pois
     * vários ClientHandlers podem chamar ao mesmo tempo.
     */
    public synchronized void adicionarUsuario(Usuario u) {
        // Remove se já existir com o mesmo nome (case-insensitive)
        usuarios.removeIf(existente ->
            existente.getNome().equalsIgnoreCase(u.getNome())
        );
        usuarios.add(u);
    }

    public Usuario buscarPorNome(String nome) {
        return usuarios.stream()
                .filter(u -> u.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public boolean existeUsuario(String nome) {
        return buscarPorNome(nome) != null;
    }
}
