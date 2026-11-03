<%@ include file="header.jspf" %>
<h1>Gestion des produits</h1>
<p><a class="btn" href="${pageContext.request.contextPath}/ProduitServlet?action=formAjouter">+ Nouveau produit</a></p>
<table>
  <tr><th>#</th><th>Désignation</th><th>Catégorie</th><th>Prix</th><th>Stock</th><th></th></tr>
  <c:forEach var="p" items="${produits}">
    <tr>
      <td>${p.id}</td>
      <td><c:out value="${p.designation}"/></td>
      <td><c:out value="${p.categorie.nom}"/></td>
      <td><fmt:formatNumber value="${p.prix}" minFractionDigits="2"/></td>
      <td>${p.stock}</td>
      <td>
        <a class="btn" href="${pageContext.request.contextPath}/ProduitServlet?action=formModifier&id=${p.id}">Modifier</a>
        <a class="btn danger conf" href="${pageContext.request.contextPath}/ProduitServlet?action=supprimer&id=${p.id}">Supprimer</a>
      </td>
    </tr>
  </c:forEach>
</table>
<script>$('.conf').on('click', function () { return confirm('Supprimer ce produit ?'); });</script>
<%@ include file="footer.jspf" %>
