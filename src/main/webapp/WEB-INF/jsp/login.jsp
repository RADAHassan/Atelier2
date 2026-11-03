<%@ include file="header.jspf" %>
<h1>Connexion</h1>
<c:if test="${not empty erreur}"><p class="erreur"><c:out value="${erreur}"/></p></c:if>
<form class="form" method="post" action="${pageContext.request.contextPath}/InternauteServlet">
  <input type="hidden" name="action" value="login">
  <label>Email <input type="email" name="email" required></label>
  <label>Mot de passe <input type="password" name="motDePasse" required></label>
  <button type="submit">Se connecter</button>
  <a href="${pageContext.request.contextPath}/InternauteServlet?action=formInscription">Créer un compte</a>
</form>
<%@ include file="footer.jspf" %>
