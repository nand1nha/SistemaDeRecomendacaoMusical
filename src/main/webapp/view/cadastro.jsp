<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    String modo = (String) request.getAttribute("modo");
    if (modo == null) modo = "cadastro";
    boolean edicao = "edicao".equals(modo);

    String nomeUsuario = (String) request.getAttribute("nomeUsuario");
    if (nomeUsuario == null) nomeUsuario = "";

    int[] avaliacoes = (int[]) request.getAttribute("avaliacoes");
    if (avaliacoes == null) avaliacoes = new int[15];

    List<String> artistas = List.of(
        "The Beatles", "Queen", "Pink Floyd", "Led Zeppelin",
        "Nirvana", "Metallica", "Iron Maiden", "U2",
        "Coldplay", "Imagine Dragons", "Legião Urbana", "Titãs",
        "Charlie Brown Jr.", "Skank", "Capital Inicial"
    );
%>
<%@ include file="_header.jspf" %>
<%@ include file="_navbar.jspf" %>

<div class="container" style="max-width: 820px;">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h1 class="h3 mb-1">
                <i class="bi bi-<%= edicao ? "pencil-square" : "person-plus-fill" %> text-primary"></i>
                <%= edicao ? "Editar usuário" : "Novo usuário" %>
            </h1>
            <p class="text-muted mb-0">
                <%= edicao
                    ? "Atualize as avaliações de " + nomeUsuario
                    : "Informe o nome e avalie os artistas de 0 a 4" %>
            </p>
        </div>
        <a href="<%= ctx %>/" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Voltar
        </a>
    </div>

    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-danger d-flex align-items-center">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <div><%= request.getAttribute("erro") %></div>
        </div>
    <% } %>

    <form action="<%= ctx %>/cadastrar" method="post">
        <input type="hidden" name="modo" value="<%= modo %>">

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <%-- Nome --%>
                <div class="mb-4">
                    <label for="nome" class="form-label fw-semibold">
                        <i class="bi bi-person"></i> Nome do usuário
                    </label>
                    <input type="text" id="nome" name="nome"
                           class="form-control form-control-lg"
                           value="<%= nomeUsuario %>"
                           <%= edicao ? "readonly" : "" %>
                           placeholder="Ex.: Ana"
                           required>
                    <% if (edicao) { %>
                        <div class="form-text">
                            <i class="bi bi-info-circle"></i>
                            O nome não pode ser alterado pois identifica o usuário.
                        </div>
                    <% } %>
                </div>

                <%-- Legenda --%>
                <div class="alert alert-light border d-flex flex-wrap gap-3 mb-4 py-2 px-3 small">
                    <span><span class="legend-dot dot-0"></span>0 — Não conheço</span>
                    <span><span class="legend-dot dot-1"></span>1 — Não gosto</span>
                    <span><span class="legend-dot dot-2"></span>2 — Gosto pouco</span>
                    <span><span class="legend-dot dot-3"></span>3 — Gosto</span>
                    <span><span class="legend-dot dot-4"></span>4 — Gosto muito</span>
                </div>

                <%-- Artistas --%>
                <% for (int i = 0; i < artistas.size(); i++) {
                       int atual = avaliacoes[i];
                %>
                    <div class="artist-row">
                        <span class="artist-name">
                            <span class="badge bg-light text-secondary border me-2"><%= i + 1 %></span>
                            <%= artistas.get(i) %>
                        </span>
                        <div class="rating-group">
                            <% for (int v = 0; v <= 4; v++) { %>
                                <label>
                                    <input type="radio"
                                           name="artista<%= i %>"
                                           value="<%= v %>"
                                           <%= (atual == v ? "checked" : "") %>>
                                    <span class="pill"><%= v %></span>
                                </label>
                            <% } %>
                        </div>
                    </div>
                <% } %>

            </div>
        </div>

        <div class="d-flex gap-2 mt-4">
            <button type="submit" class="btn btn-primary btn-lg">
                <i class="bi bi-<%= edicao ? "save" : "check-lg" %>"></i>
                <%= edicao ? "Salvar alterações" : "Cadastrar usuário" %>
            </button>
            <a href="<%= ctx %>/" class="btn btn-outline-secondary btn-lg">
                <i class="bi bi-x-lg"></i> Cancelar
            </a>
        </div>

    </form>

</div>

<%@ include file="_footer.jspf" %>