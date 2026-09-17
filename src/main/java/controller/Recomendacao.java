package controller;

import java.io.IOException;

import infrastructure.client.SocketClientService;
import infrastructure.protocol.Protocolo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/recomendar")
public class Recomendacao extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redireciona GET para a home (recomendação só faz sentido via POST)
        resp.sendRedirect(req.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");

        try {
            String resposta = SocketClientService.obterRecomendacoes(nome);
            String[] partes = resposta.split("\\|", 2);

            if (Protocolo.OK.equals(partes[0])) {
                req.setAttribute("recomendacoes", partes.length > 1 ? partes[1] : "");
                req.setAttribute("nome", nome);
                req.getRequestDispatcher("/view/resultado.jsp").forward(req, resp);
            } else {
                req.setAttribute("erro", partes.length > 1 ? partes[1] : "Erro desconhecido.");
                req.getRequestDispatcher("/view/index.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao recomendar: " + e.getMessage());
            req.getRequestDispatcher("/view/index.jsp").forward(req, resp);
        }
    }
}
