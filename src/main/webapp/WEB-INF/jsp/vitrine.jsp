<%@ include file="header.jspf" %>
<h1>Vitrine</h1>

<form class="filtres" method="get" action="${pageContext.request.contextPath}/VitrineServlet">
  <input type="hidden" name="action" value="rechercher">
  <input type="text" id="motCle" name="motCle" placeholder="Rechercher un produit…" value="<c:out value='${motCle}'/>">
  <select name="categorie">
    <option value="">Toutes les catégories</option>
    <c:forEach var="c" items="${categories}">
      <option value="${c.id}" ${c.id == categorieId ? 'selected' : ''}><c:out value="${c.nom}"/></option>
    </c:forEach>
  </select>
  <button type="submit">Filtrer</button>
</form>

<c:if test="${empty produits}"><p>Aucun produit trouvé.</p></c:if>
<div class="grid">
  <c:forEach var="p" items="${produits}">
    <div class="card" data-nom="<c:out value='${fn:toLowerCase(p.designation)}'/>">
      <span class="cat"><c:out value="${p.categorie.nom}"/></span>
      <strong><a href="${pageContext.request.contextPath}/VitrineServlet?action=detail&id=${p.id}"><c:out value="${p.designation}"/></a></strong>
      <span><c:out value="${p.description}"/></span>
      <span class="prix"><fmt:formatNumber value="${p.prix}" minFractionDigits="2"/> MAD</span>
      <c:choose>
        <c:when test="${p.stock > 0}">
          <form method="post" action="${pageContext.request.contextPath}/PanierServlet">
            <input type="hidden" name="action" value="ajouter">
            <input type="hidden" name="retour" value="vitrine">
            <input type="hidden" name="produitId" value="${p.id}">
            <input type="number" name="quantite" value="1" min="1" max="${p.stock}">
            <button type="submit">Ajouter au panier</button>
          </form>
        </c:when>
        <c:otherwise><em>Rupture de stock</em></c:otherwise>
      </c:choose>
    </div>
  </c:forEach>
</div>

<script>
  // jQuery : filtrage instantané côté client pendant la saisie
  $('#motCle').on('input', function () {
    var q = $(this).val().toLowerCase();
    $('.card').each(function () { $(this).toggle($(this).data('nom').indexOf(q) !== -1); });
  });
</script>
<%@ include file="footer.jspf" %>
