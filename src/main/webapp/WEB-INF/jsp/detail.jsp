<%@ include file="header.jspf" %>
<h1><c:out value="${produit.designation}"/></h1>
<div class="card">
  <span class="cat"><c:out value="${produit.categorie.nom}"/></span>
  <p><c:out value="${produit.description}"/></p>
  <span class="prix"><fmt:formatNumber value="${produit.prix}" minFractionDigits="2"/> MAD</span>
  <span>Stock : ${produit.stock}</span>
  <c:if test="${produit.stock > 0}">
    <form method="post" action="${pageContext.request.contextPath}/PanierServlet">
      <input type="hidden" name="action" value="ajouter">
      <input type="hidden" name="produitId" value="${produit.id}">
      <input type="number" name="quantite" value="1" min="1" max="${produit.stock}">
      <button type="submit">Ajouter au panier</button>
    </form>
  </c:if>
</div>
<p><a class="btn sec" href="${pageContext.request.contextPath}/VitrineServlet?action=lister">← Retour à la vitrine</a></p>
<%@ include file="footer.jspf" %>
