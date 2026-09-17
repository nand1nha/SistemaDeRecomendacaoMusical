package model.domain;

public class Artista {
    private int codigo;
    private String nome;

    public Artista(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }
}
