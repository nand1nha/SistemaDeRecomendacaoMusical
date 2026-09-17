package infrastructure.server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import infrastructure.protocol.Protocolo;
import model.application.CalculoRecomendacao;
import model.application.GerenciadorDados;
import model.domain.Usuario;

public class ClientHandler implements Runnable {
        private final Socket socket;
        private final GerenciadorDados dados;

    public ClientHandler(Socket socket, GerenciadorDados dados) {
        this.socket = socket;
        this.dados = dados;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String linha;
            while ((linha = in.readLine()) != null) {
                System.out.println("[Cliente " + socket.getInetAddress() + "] " + linha);
                String resposta = processarComando(linha);
                out.println(resposta);
            }
        } catch (IOException e) {
            System.err.println("Erro com cliente: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private String processarComando(String comando) {
        try {
            String[] partes = comando.split("\\|", 3);
            if (partes.length == 0) return Protocolo.ERRO + "|Comando vazio";

            String acao = partes[0].toUpperCase();

            switch (acao) {
                case "CADASTRAR": return cadastrar(partes);
                case "LISTAR":    return listar();
                case "RECOMENDAR":return recomendar(partes);
                case "BUSCAR":    return buscar(partes);
                case "QUIT":
                case "SAIR":      return "__ENCERRAR__";
                default:          return Protocolo.ERRO + "|Comando desconhecido: " + acao;
            }
        } catch (Exception e) {
            return Protocolo.ERRO + "|" + e.getMessage();
        }
    }

    private String buscar(String[] partes) {
        if (partes.length < 2) return Protocolo.ERRO + "|Formato: BUSCAR|nome";

        String nome = partes[1].trim();
        Usuario u = dados.buscarPorNome(nome);
        if (u == null) return Protocolo.ERRO + "|Usuário '" + nome + "' não encontrado";

        int[] av = u.getAvaliacoes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            if (i > 0) sb.append(",");
            sb.append(av[i]);
        }
        return Protocolo.OK + "|" + sb.toString();
    }

    private String cadastrar(String[] partes) {
        if (partes.length < 3) return Protocolo.ERRO + "|Formato: CADASTRAR|nome|v1,...,v15";

        String nome = partes[1].trim();
        if (nome.isEmpty()) return Protocolo.ERRO + "|Nome não pode ser vazio";

        String[] valoresStr = partes[2].split(",");
        if (valoresStr.length != 15) {
            return Protocolo.ERRO + "|Esperado 15 avaliações, recebido " + valoresStr.length;
        }

        int[] avaliacoes = new int[15];
        for (int i = 0; i < 15; i++) {
            int v = Integer.parseInt(valoresStr[i].trim());
            if (v < 0 || v > 4) {
                return Protocolo.ERRO + "|Nota inválida em " + dados.getArtistas().get(i) + ": " + v;
            }
            avaliacoes[i] = v;
        }

        dados.adicionarUsuario(new Usuario(nome, avaliacoes));
        return Protocolo.OK + "|Usuário " + nome + " cadastrado com sucesso";
    }

    private String listar() {
        List<String> nomes = dados.getNomesUsuarios();
        if (nomes.isEmpty()) return Protocolo.OK + "|";
        return Protocolo.OK + "|" + String.join(",", nomes);
    }

    private String recomendar(String[] partes) {
        if (partes.length < 2) return Protocolo.ERRO + "|Formato: RECOMENDAR|nome";

        String nome = partes[1].trim();
        Usuario alvo = dados.buscarPorNome(nome);
        if (alvo == null) {
            return Protocolo.ERRO + "|Usuário '" + nome + "' não encontrado";
        }

        List<String> recomendacoes = CalculoRecomendacao.recomendar(
            alvo, dados.getUsuarios(), dados.getArtistas()
        );

        if (recomendacoes.isEmpty()) {
            return Protocolo.OK + "|Nenhuma recomendação encontrada.";
        }
        return Protocolo.OK + "|" + String.join(",", recomendacoes);
    }
}
