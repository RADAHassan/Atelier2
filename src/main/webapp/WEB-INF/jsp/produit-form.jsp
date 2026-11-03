<%@ include file="header.jspf" %>
<c:set var="edition" value="${not empty produit}"/>
<h1>${edition ? 'Modifier' : 'Nouveau'} produit</h1>
<form class="form" method="post" action="${pageContext.request.contextPath}/ProduitServlet">
  <input type="hidden" name="action" value="${edition ? 'modifier' : 'ajouter'}">
  <c:if test="${edition}"><input type="hidden" name="id" value="${produit.id}"></c:if>
  <label>Désignation <input type="text" name="designation" value="<c:out value='${produit.designation}'/>" required></label>
  <label>Description <textarea name="description" rows="3"><c:out value="${produit.description}"/></textarea></label>
  <label>Prix (MAD) <input type="number" step="0.01" min="0" name="prix" value="${produit.prix}" style="width:auto" required></label>
  <label>Stock <input type="number" min="0" name="stock" value="${produit.stock}" style="width:auto" required></label>
  <label>Image (URL, optionnel) <input type="text" name="image" value="<c:out value='${produit.image}'/>"></label>
  <label>Catégorie
    <select name="categorie">
      <c:forEach var="c" items="${categories}">
        <option value="${c.id}" ${c.id == produit.categorie.id ? 'selected' : ''}><c:out value="${c.nom}"/></option>
      </c:forEach>
    </select>
  </label>
  <div><button type="submit">Enregistrer</button>
  <a class="btn sec" href="${pageContext.request.contextPath}/ProduitServlet?action=lister">Annuler</a></div>
</form>
<%@ include file="footer.jspf" %>
