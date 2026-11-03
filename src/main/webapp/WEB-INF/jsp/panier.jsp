<%@ include file="header.jspf" %>
<h1>Mon panier</h1>
<c:choose>
  <c:when test="${empty sessionScope.panier.lignes}">
    <p>Votre panier est vide.</p>
  </c:when>
  <c:otherwise>
    <table>
      <tr><th>Produit</th><th>Prix unitaire</th><th>Quantité</th><th>Sous-total</th><th></th></tr>
      <c:forEach var="l" items="${sessionScope.panier.lignes}">
        <tr>
          <td><c:out value="${l.produit.designation}"/></td>
          <td><fmt:formatNumber value="${l.produit.prix}" minFractionDigits="2"/> MAD</td>
          <td>
            <form method="post" action="${pageContext.request.contextPath}/PanierServlet">
              <input type="hidden" name="action" value="modifier">
              <input type="hidden" name="produitId" value="${l.produit.id}">
              <input class="qte" type="number" name="quantite" value="${l.quantite}" min="0" max="${l.produit.stock}">
            </form>
          </td>
          <td><fmt:formatNumber value="${l.sousTotal}" minFractionDigits="2"/> MAD</td>
          <td><a class="btn danger" href="${pageContext.request.contextPath}/PanierServlet?action=retirer&produitId=${l.produit.id}">Retirer</a></td>
        </tr>
      </c:forEach>
    </table>
    <p class="total">Total : <strong><fmt:formatNumber value="${sessionScope.panier.total}" minFractionDigits="2"/> MAD</strong></p>
    <a class="btn danger" id="vider" href="${pageContext.request.contextPath}/PanierServlet?action=vider">Vider le panier</a>
  </c:otherwise>
</c:choose>
<a class="btn sec" href="${pageContext.request.contextPath}/VitrineServlet?action=lister">Continuer mes achats</a>
<script>
  $('.qte').on('change', function () { $(this).closest('form').submit(); });
  $('#vider').on('click', function () { return confirm('Vider tout le panier ?'); });
</script>
<%@ include file="footer.jspf" %>
