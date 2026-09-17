<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ include file="_header.jspf" %>
<%@ include file="_navbar.jspf" %>

<div class="container" style="max-width: 900px;">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <h1 class="h3 mb-1">
                <i class="bi bi-people-fill text-primary"></i> Usuários cadastrados
            </h1>
            <p class="text-muted mb-0">Gerencie usuários e gere recomendações personalizadas</p>
        </div>
        <a href="<%= ctx %>/cadastrar" class="btn btn-primary">
            <i class="bi bi-person-plus-fill"></i> Novo usuário
        </a>
    </div>

    <%-- Alertas --%>
    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-danger d-flex align-items-center" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <div><%= request.getAttribute("erro") %></div>
        </div>
    <% } %>
    <% if (request.getAttribute("sucesso") != null) { %>
        <div class="alert alert-success d-flex align-items-center alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>
            <div><%= request.getAttribute("sucesso") %></div>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <%
        List<String> usuarios = (List<String>) request.getAttribute("usuarios");
        if (usuarios == null || usuarios.isEmpty()) {
    %>
        <div class="card border-0 shadow-sm">
            <div class="card-body text-center py-5">
                <i class="bi bi-inbox text-muted" style="font-size:3rem;"></i>
                <h5 class="mt-3 text-muted">Nenhum usuário cadastrado ainda</h5>
                <p class="text-muted">Comece cadastrando um usuário para receber recomendações.</p>
                <a href="<%= ctx %>/cadastrar" class="btn btn-primary">
                    <i class="bi bi-person-plus"></i> Cadastrar primeiro usuário
                </a>
            </div>
        </div>
    <% } else { %>
        <div class="card border-0 shadow-sm">
            <ul class="list-group list-group-flush">
                <% for (String u : usuarios) {
                    String inicial = u.substring(0, 1).toUpperCase();
                    String encoded = java.net.URLEncoder.encode(u, "UTF-8");
                %>
                    <li class="list-group-item d-flex align-items-center justify-content-between py-3">
                        <div class="d-flex align-items-center gap-3">
                            <span class="user-avatar"><%= inicial %></span>
                            <div>
                                <div class="fw-semibold"><%= u %></div>
                                <small class="text-muted">Perfil musical cadastrado</small>
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <a href="<%= ctx %>/editar?nome=<%= encoded %>"
                               class="btn btn-outline-secondary btn-sm">
                                <i class="bi bi-pencil-square"></i> Editar
                            </a>
                            <form action="<%= ctx %>/recomendar" method="post" class="m-0">
                                <input type="hidden" name="nome" value="<%= u %>">
                                <button type="submit" class="btn btn-success btn-sm">
                                    <i class="bi bi-stars"></i> Recomendar
                                </button>
                            </form>
                        </div>
                    </li>
                <% } %>
            </ul>
        </div>
    <% } %>

</div>

<%@ include file="_footer.jspf" %>