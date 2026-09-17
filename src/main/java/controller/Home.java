package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import infrastructure.client.SocketClientService;
import infrastructure.protocol.Protocolo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Busca a lista de usuários no servidor TCP
        try {
            String resposta = SocketClientService.listarUsuarios();
            String[] partes = resposta.split("\\|", 2);

            List<String> usuarios = new ArrayList<>();
            if (Protocolo.OK.equals(partes[0])
                    && partes.length > 1
                    && !partes[1].isBlank()) {
                usuarios = Arrays.asList(partes[1].split(","));
            }
            req.setAttribute("usuarios", usuarios);

        } catch (IOException e) {
            req.setAttribute("erro",
                "Não foi possível conectar ao servidor TCP: " + e.getMessage());
            req.setAttribute("usuarios", new ArrayList<String>());
        }

        // 2. Recupera o flash de sucesso (se houver) e remove da sessão
        Object sucesso = req.getSession().getAttribute("sucesso");
        if (sucesso != null) {
            req.setAttribute("sucesso", sucesso);
            req.getSession().removeAttribute("sucesso");
        }

        // 3. Encaminha para a View
        req.getRequestDispatcher("/view/index.jsp").forward(req, resp);
    }
    
}
