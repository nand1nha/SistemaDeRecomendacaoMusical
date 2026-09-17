package controller;

import java.io.IOException;

import infrastructure.client.SocketClientService;
import infrastructure.protocol.Protocolo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editar")
public class EditarAvalicao extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");
        if (nome == null || nome.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        try {
            String resposta = SocketClientService.buscarUsuario(nome);
            String[] partes = resposta.split("\\|", 2);

            if (!Protocolo.OK.equals(partes[0]) || partes.length < 2) {
                req.setAttribute("erro", partes.length > 1 ? partes[1] : "Usuário não encontrado.");
                req.getRequestDispatcher("/view/index.jsp").forward(req, resp);
                return;
            }

            // Converte "4,3,4,..." em int[]
            String[] vals = partes[1].split(",");
            int[] avaliacoes = new int[15];
            for (int i = 0; i < 15; i++) {
                avaliacoes[i] = Integer.parseInt(vals[i].trim());
            }

            req.setAttribute("modo", "edicao");
            req.setAttribute("nomeUsuario", nome);
            req.setAttribute("avaliacoes", avaliacoes);
            req.getRequestDispatcher("/view/cadastro.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao buscar usuário: " + e.getMessage());
            req.getRequestDispatcher("/view/index.jsp").forward(req, resp);
        }
    }
}
