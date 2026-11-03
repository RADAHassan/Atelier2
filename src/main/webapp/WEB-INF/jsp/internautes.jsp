<%@ include file="header.jspf" %>
<h1>Internautes</h1>
<table>
  <tr><th>#</th><th>Nom</th><th>Prénom</th><th>Email</th><th></th></tr>
  <c:forEach var="i" items="${internautes}">
    <tr>
      <td>${i.id}</td>
      <td><c:out value="${i.nom}"/></td>
      <td><c:out value="${i.prenom}"/></td>
      <td><c:out value="${i.email}"/></td>
      <td><a class="btn danger conf" href="${pageContext.request.contextPath}/InternauteServlet?action=supprimer&id=${i.id}">Supprimer</a></td>
    </tr>
  </c:forEach>
</table>
<script>$('.conf').on('click', function () { return confirm('Supprimer cet internaute ?'); });</script>
<%@ include file="footer.jspf" %>
