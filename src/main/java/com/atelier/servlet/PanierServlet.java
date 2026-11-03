package com.atelier.servlet;

import com.atelier.dao.PanierDao;
import com.atelier.model.Internaute;
import com.atelier.model.Panier;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Gestion du panier : afficher, ajouter, modifier, retirer, vider. */
@WebServlet("/PanierServlet")
public class PanierServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final PanierDao panierDao = new PanierDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "afficher";
        HttpSession session = request.getSession();
        Panier panier = panierCourant(session);
        Long produitId = toLong(request.getParameter("produitId"));

        if ("ajouter".equals(action)) {
            if (produitId != null)
                session.setAttribute("panier", panierDao.ajouter(panier.getId(), produitId, toInt(request.getParameter("quantite"), 1)));
            String retour = request.getParameter("retour");
            redirect(request, response, "vitrine".equals(retour)
                    ? "/VitrineServlet?action=lister" : "/PanierServlet?action=afficher");

        } else if ("modifier".equals(action)) {
            if (produitId != null)
                session.setAttribute("panier", panierDao.modifierQuantite(panier.getId(), produitId, toInt(request.getParameter("quantite"), 1)));
            redirect(request, response, "/PanierServlet?action=afficher");

        } else if ("retirer".equals(action)) {
            if (produitId != null) session.setAttribute("panier", panierDao.retirer(panier.getId(), produitId));
            redirect(request, response, "/PanierServlet?action=afficher");

        } else if ("vider".equals(action)) {
            session.setAttribute("panier", panierDao.vider(panier.getId()));
            redirect(request, response, "/PanierServlet?action=afficher");

        } else { // afficher
            session.setAttribute("panier", panierDao.find(panier.getId()));
            forward(request, response, "panier.jsp");
        }
    }

    /** Panier de la session, créé (et persisté) à la demande. */
    private Panier panierCourant(HttpSession session) {
        Panier p = (Panier) session.getAttribute("panier");
        if (p == null || p.getId() == null) {
            p = panierDao.creer((Internaute) session.getAttribute("user"));
            session.setAttribute("panier", p);
        }
        return p;
    }
}
