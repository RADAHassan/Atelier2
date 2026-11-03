package com.atelier.servlet;

import com.atelier.dao.InternauteDao;
import com.atelier.dao.PanierDao;
import com.atelier.model.Internaute;
import com.atelier.model.Panier;
import com.atelier.util.Securite;
import java.io.IOException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Gestion des internautes : formInscription, inscrire, formLogin, login, logout, lister, supprimer. */
@WebServlet("/InternauteServlet")
public class InternauteServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private final InternauteDao internauteDao = new InternauteDao();
    private final PanierDao panierDao = new PanierDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "formLogin";

        if ("formInscription".equals(action)) {
            forward(request, response, "inscription.jsp");

        } else if ("inscrire".equals(action)) {
            String nom = request.getParameter("nom"), prenom = request.getParameter("prenom");
            String email = request.getParameter("email"), mdp = request.getParameter("motDePasse");
            if (vide(nom) || vide(prenom) || vide(email) || vide(mdp)) {
                request.setAttribute("erreur", "Tous les champs sont obligatoires.");
                forward(request, response, "inscription.jsp");
            } else if (internauteDao.findByEmail(email.trim()) != null) {
                request.setAttribute("erreur", "Cet email est déjà utilisé.");
                forward(request, response, "inscription.jsp");
            } else {
                try {
                    Internaute i = internauteDao.inscrire(
                            new Internaute(nom.trim(), prenom.trim(), email.trim(), Securite.sha256(mdp)));
                    connecter(request, i);
                    redirect(request, response, "/VitrineServlet?action=lister");
                } catch (PersistenceException e) {
                    request.setAttribute("erreur", "Inscription impossible : " + e.getMessage());
                    forward(request, response, "inscription.jsp");
                }
            }

        } else if ("login".equals(action)) {
            String email = request.getParameter("email"), mdp = request.getParameter("motDePasse");
            Internaute i = vide(email) ? null : internauteDao.findByEmail(email.trim());
            if (i == null || vide(mdp) || !i.getMotDePasse().equals(Securite.sha256(mdp))) {
                request.setAttribute("erreur", "Email ou mot de passe incorrect.");
                forward(request, response, "login.jsp");
            } else {
                connecter(request, i);
                redirect(request, response, "/VitrineServlet?action=lister");
            }

        } else if ("logout".equals(action)) {
            HttpSession s = request.getSession(false);
            if (s != null) s.invalidate();
            redirect(request, response, "/VitrineServlet?action=lister");

        } else if ("lister".equals(action)) {
            request.setAttribute("internautes", internauteDao.findAll());
            forward(request, response, "internautes.jsp");

        } else if ("supprimer".equals(action)) {
            Long id = toLong(request.getParameter("id"));
            Internaute courant = (Internaute) request.getSession().getAttribute("user");
            if (id != null && (courant == null || !id.equals(courant.getId()))) internauteDao.supprimer(id);
            redirect(request, response, "/InternauteServlet?action=lister");

        } else { // formLogin
            forward(request, response, "login.jsp");
        }
    }

    /** Ouvre la session et rattache/récupère le panier de l'internaute. */
    private void connecter(HttpServletRequest request, Internaute i) {
        HttpSession session = request.getSession();
        session.setAttribute("user", i);
        Panier invite = (Panier) session.getAttribute("panier");
        Panier existant = panierDao.findByInternaute(i);
        if (invite != null && invite.getId() != null && !invite.getLignes().isEmpty()) {
            session.setAttribute("panier", panierDao.rattacher(invite.getId(), i)); // le panier d'invité est conservé
        } else if (existant != null) {
            session.setAttribute("panier", existant);
        } else {
            session.setAttribute("panier", panierDao.creer(i));
        }
    }

    private static boolean vide(String s) { return s == null || s.trim().isEmpty(); }
}
