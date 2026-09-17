<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nome = (String) request.getAttribute("nome");
    String recomendacoes = (String) request.getAttribute("recomendacoes");
    String[] lista = (recomendacoes == null || recomendacoes.isBlank())
                        ? new String[0]
                        : recomendacoes.split(",");
%>
<%@ include file="_header.jspf" %>
<%@ include file="_navbar.jspf" %>

<div class="container" style="max-width: 720px;">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h1 class="h3 mb-1">
                <i class="bi bi-stars text-warning"></i> Recomendações
            </h1>
            <p class="text-muted mb-0">
                Sugestões personalizadas para <strong><%= nome %></strong>
            </p>
        </div>
        <a href="<%= ctx %>/" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Voltar
        </a>
    </div>

    <div class="card border-0 shadow-sm">
        <div class="card-body p-4">

            <% if (lista.length == 0) { %>
                <div class="text-center py-4">
                    <i class="bi bi-emoji-neutral text-muted" style="font-size:3rem;"></i>
                    <h5 class="mt-3 text-muted">Nenhuma recomendação encontrada</h5>
                    <p class="text-muted mb-0">
                        Cadastre mais usuários com gostos variados
                        para melhorar as sugestões.
                    </p>
                </div>
            <% } else { %>
                <p class="text-muted small mb-3">
                    <i class="bi bi-info-circle"></i>
                    Baseado nos usuários com gostos mais parecidos com os seus:
                </p>
                <% for (String artista : lista) { %>
                    <div class="recomendacao-item">
                        <i class="bi bi-music-note-beamed"></i>
                        <span><%= artista.trim() %></span>
                    </div>
                <% } %>
            <% } %>

        </div>
    </div>

    <div class="d-flex gap-2 mt-4">
        <a href="<%= ctx %>/" class="btn btn-primary">
            <i class="bi bi-house-door"></i> Voltar para home
        </a>
        <a href="<%= ctx %>/editar?nome=<%= java.net.URLEncoder.encode(nome, "UTF-8") %>"
           class="btn btn-outline-secondary">
            <i class="bi bi-pencil-square"></i> Editar minhas avaliações
        </a>
    </div>

</div>

<%@ include file="_footer.jspf" %>