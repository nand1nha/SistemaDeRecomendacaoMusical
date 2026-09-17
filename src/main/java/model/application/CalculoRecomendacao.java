package model.application;

import java.util.*;

import model.domain.Usuario;

public class CalculoRecomendacao {

    /**
     * Calcula a distância euclidiana entre dois usuários,
     * considerando apenas posições onde AMBOS têm nota != 0.
     */
    public static double calcularDistancia(Usuario a, Usuario b) {
        int[] notasA = a.getAvaliacoes();
        int[] notasB = b.getAvaliacoes();

        double soma = 0.0;
        int posicoesConsideradas = 0;

        for (int i = 0; i < 15; i++) {
            if (notasA[i] != 0 && notasB[i] != 0) {
                double diff = notasA[i] - notasB[i];
                soma += diff * diff;
                posicoesConsideradas++;
            }
        }

        if (posicoesConsideradas == 0) {
            return Double.MAX_VALUE;
        }

        return Math.sqrt(soma);
    }

    /**
     * Gera recomendações para um usuário-alvo.
     */
    public static List<String> recomendar(Usuario alvo,
                                          List<Usuario> todos,
                                          List<String> artistas) {

        // 1. Distância do alvo para todos os outros usuários
        List<Map.Entry<Usuario, Double>> distancias = new ArrayList<>();
        for (Usuario u : todos) {
            if (u.getNome().equals(alvo.getNome())) continue;
            double d = calcularDistancia(alvo, u);
            if (d < Double.MAX_VALUE) {
                distancias.add(new AbstractMap.SimpleEntry<>(u, d));
            }
        }

        // 2. Ordena por distância crescente
        distancias.sort(Comparator.comparingDouble(Map.Entry::getValue));

        // 3. Top 3 mais similares
        int limite = Math.min(3, distancias.size());
        List<Usuario> maisSimilares = new ArrayList<>();
        for (int i = 0; i < limite; i++) {
            maisSimilares.add(distancias.get(i).getKey());
        }

        // 4. Recomenda artistas que o alvo NÃO conhece (nota 0)
        //    e que usuários similares avaliaram bem (nota >= 3)
        int[] notasAlvo = alvo.getAvaliacoes();
        Map<String, Double> pontuacaoArtistas = new HashMap<>();

        for (Usuario similar : maisSimilares) {
            int[] notasSim = similar.getAvaliacoes();
            for (int i = 0; i < 15; i++) {
                if (notasAlvo[i] == 0 && notasSim[i] >= 3) {
                    String artista = artistas.get(i);
                    pontuacaoArtistas.merge(artista, (double) notasSim[i], Double::sum);
                }
            }
        }

        // 5. Ordena por pontuação decrescente
        return pontuacaoArtistas.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
    }
}