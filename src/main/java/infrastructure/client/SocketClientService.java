package infrastructure.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientService {
    private static final String HOST = "localhost";
    private static final int PORTA = 9876;

    /** Envia um comando bruto ao servidor e retorna a resposta. */
    public static String enviar(String comando) throws IOException {
        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(comando);
            String resposta = in.readLine();
            return resposta == null ? "ERRO|Servidor não respondeu." : resposta;
        }
    }

    public static String cadastrarUsuario(String nome, int[] avaliacoes) throws IOException {
        StringBuilder sb = new StringBuilder("CADASTRAR|").append(nome).append("|");
        for (int i = 0; i < 15; i++) {
            if (i > 0) sb.append(",");
            sb.append(avaliacoes[i]);
        }
        return enviar(sb.toString());
    }

    public static String listarUsuarios() throws IOException {
        return enviar("LISTAR");
    }

    public static String obterRecomendacoes(String nome) throws IOException {
        return enviar("RECOMENDAR|" + nome);
    }

    public static String buscarUsuario(String nome) throws IOException {
    return enviar("BUSCAR|" + nome);
}
}
