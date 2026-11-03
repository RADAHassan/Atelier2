<%@ include file="header.jspf" %>
<h1>Inscription</h1>
<c:if test="${not empty erreur}"><p class="erreur"><c:out value="${erreur}"/></p></c:if>
<form class="form" method="post" action="${pageContext.request.contextPath}/InternauteServlet">
  <input type="hidden" name="action" value="inscrire">
  <label>Nom <input type="text" name="nom" value="<c:out value='${param.nom}'/>" required></label>
  <label>Prénom <input type="text" name="prenom" value="<c:out value='${param.prenom}'/>" required></label>
  <label>Email <input type="email" name="email" value="<c:out value='${param.email}'/>" required></label>
  <label>Mot de passe <input type="password" name="motDePasse" required></label>
  <button type="submit">S'inscrire</button>
</form>
<%@ include file="footer.jspf" %>
