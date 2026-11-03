package com.atelier.servlet;

import com.atelier.dao.ProduitDao;
import com.atelier.model.Produit;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Gestion des produits (administration) : liste, formAjouter, ajouter, formModifier, modifier, supprimer. */
@WebServlet("/ProduitServlet")
public class ProduitServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final ProduitDao produitDao = new ProduitDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "lister";

        if ("formAjouter".equals(action)) {
            request.setAttribute("categories", produitDao.findCategories());
            forward(request, response, "produit-form.jsp");

        } else if ("ajouter".equals(action)) {
            produitDao.enregistrer(lire(request, new Produit()));
            redirect(request, response, "/ProduitServlet?action=lister");

        } else if ("formModifier".equals(action)) {
            Produit p = produitDao.find(toLong(request.getParameter("id")));
            if (p == null) { redirect(request, response, "/ProduitServlet?action=lister"); return; }
            request.setAttribute("produit", p);
            request.setAttribute("categories", produitDao.findCategories());
            forward(request, response, "produit-form.jsp");

        } else if ("modifier".equals(action)) {
            Produit p = produitDao.find(toLong(request.getParameter("id")));
            if (p != null) produitDao.enregistrer(lire(request, p));
            redirect(request, response, "/ProduitServlet?action=lister");

        } else if ("supprimer".equals(action)) {
            Long id = toLong(request.getParameter("id"));
            if (id != null) produitDao.supprimer(id);
            redirect(request, response, "/ProduitServlet?action=lister");

        } else { // lister
            request.setAttribute("produits", produitDao.findAll());
            forward(request, response, "produits-admin.jsp");
        }
    }

    private Produit lire(HttpServletRequest r, Produit p) {
        p.setDesignation(r.getParameter("designation"));
        p.setDescription(r.getParameter("description"));
        p.setPrix(toDouble(r.getParameter("prix"), 0));
        p.setStock(Math.max(0, toInt(r.getParameter("stock"), 0)));
        String img = r.getParameter("image");
        p.setImage(img == null || img.trim().isEmpty() ? null : img.trim());
        Long cid = toLong(r.getParameter("categorie"));
        p.setCategorie(cid == null ? null : produitDao.findCategorie(cid));
        return p;
    }
}
