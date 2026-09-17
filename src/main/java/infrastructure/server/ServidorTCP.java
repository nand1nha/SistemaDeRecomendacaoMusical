package infrastructure.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.application.GerenciadorDados;

public class ServidorTCP {
        public static final int PORTA = 9876;

    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        ExecutorService pool = Executors.newFixedThreadPool(50);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor TCP iniciado na porta " + PORTA);
            System.out.println("Artistas carregados: " + dados.getArtistas().size());
            System.out.println("Usuários cadastrados: " + dados.getUsuarios().size());

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                pool.execute(new ClientHandler(cliente, dados));
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}
