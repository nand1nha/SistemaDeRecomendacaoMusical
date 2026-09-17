package controller;

import java.io.IOException;

import infrastructure.client.SocketClientService;
import infrastructure.protocol.Protocolo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cadastrar")
public class CadastroUsuario extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setAttribute("modo", "cadastro");
        // Abre o formulário de cadastro
        req.getRequestDispatcher("/view/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome = req.getParameter("nome");
        int[] avaliacoes = new int[15];

        try {
            for (int i = 0; i < 15; i++) {
                String valor = req.getParameter("artista" + i);
                avaliacoes[i] = (valor == null || valor.isEmpty()) ? 0 : Integer.parseInt(valor);
            }

            String resposta = SocketClientService.cadastrarUsuario(nome, avaliacoes);
            String[] partes = resposta.split("\\|", 2);

            if (Protocolo.OK.equals(partes[0])) {
                // Sucesso: volta para a home (que recarrega a lista)
                req.getSession().setAttribute("sucesso",
                    Protocolo.OK.equals(partes[0]) ? "Usuário salvo com sucesso!" : null);
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                req.setAttribute("erro", partes.length > 1 ? partes[1] : "Erro desconhecido.");
                req.getRequestDispatcher("/view/cadastro.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            req.getRequestDispatcher("/view/cadastro.jsp").forward(req, resp);
        }
    }
}
