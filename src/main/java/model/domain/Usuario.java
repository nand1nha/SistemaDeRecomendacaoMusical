package model.domain;

import java.util.Arrays;

public class Usuario {
    private String nome;
    private int[] avaliacoes; 

    public Usuario(String nome, int[] avaliacoes) {
        if (avaliacoes == null || avaliacoes.length != 15) {
            throw new IllegalArgumentException("O vetor de avaliações deve ter 15 posições.");
        }
        this.nome = nome;
        this.avaliacoes = Arrays.copyOf(avaliacoes, 15);
    }

    public String getNome() { return nome; }
    public int[] getAvaliacoes() { return Arrays.copyOf(avaliacoes, 15); }
    public int getAvaliacao(int indice) { return avaliacoes[indice]; }

    @Override
    public String toString() {
        return nome + " " + Arrays.toString(avaliacoes);
    }
}
