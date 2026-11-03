package com.atelier.servlet;

import com.atelier.dao.ProduitDao;
import com.atelier.model.Produit;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Vitrine : consultation du catalogue (actions : lister, rechercher, detail). */
@WebServlet("/VitrineServlet")
public class VitrineServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final ProduitDao produitDao = new ProduitDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "lister";

        if ("detail".equals(action)) {
            Produit p = produitDao.find(toLong(request.getParameter("id")));
            if (p == null) { redirect(request, response, "/VitrineServlet?action=lister"); return; }
            request.setAttribute("produit", p);
            forward(request, response, "detail.jsp");
        } else { // lister / rechercher
            String motCle = request.getParameter("motCle");
            Long cat = toLong(request.getParameter("categorie"));
            request.setAttribute("produits", produitDao.rechercher(motCle, cat));
            request.setAttribute("categories", produitDao.findCategories());
            request.setAttribute("motCle", motCle);
            request.setAttribute("categorieId", cat);
            forward(request, response, "vitrine.jsp");
        }
    }
}
