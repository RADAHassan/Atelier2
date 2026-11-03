package com.atelier.dao;

import com.atelier.model.Categorie;
import com.atelier.model.LignePanier;
import com.atelier.model.Panier;
import com.atelier.model.Produit;
import com.atelier.util.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProduitDao {

    public List<Produit> findAll() {
        EntityManager em = JPAUtil.em();
        try { return em.createQuery("SELECT p FROM Produit p ORDER BY p.designation", Produit.class).getResultList(); }
        finally { em.close(); }
    }

    public List<Produit> rechercher(String motCle, Long categorieId) {
        EntityManager em = JPAUtil.em();
        try {
            boolean mc = motCle != null && !motCle.trim().isEmpty();
            StringBuilder q = new StringBuilder("SELECT p FROM Produit p WHERE 1=1");
            if (mc) q.append(" AND LOWER(p.designation) LIKE :mc");
            if (categorieId != null) q.append(" AND p.categorie.id = :cid");
            q.append(" ORDER BY p.designation");
            TypedQuery<Produit> query = em.createQuery(q.toString(), Produit.class);
            if (mc) query.setParameter("mc", "%" + motCle.trim().toLowerCase() + "%");
            if (categorieId != null) query.setParameter("cid", categorieId);
            return query.getResultList();
        } finally { em.close(); }
    }

    public Produit find(Long id) {
        EntityManager em = JPAUtil.em();
        try { return em.find(Produit.class, id); } finally { em.close(); }
    }

    /** Persiste un nouveau produit ou fusionne un produit existant. */
    public Produit enregistrer(Produit p) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Produit r;
            if (p.getId() == null) { em.persist(p); r = p; } else { r = em.merge(p); }
            em.getTransaction().commit();
            return r;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    public void supprimer(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            // retire d'abord le produit des paniers
            List<LignePanier> lignes = em
                    .createQuery("SELECT l FROM LignePanier l WHERE l.produit.id = :id", LignePanier.class)
                    .setParameter("id", id).getResultList();
            for (LignePanier l : lignes) {
                Panier p = l.getPanier();
                p.getLignes().remove(l); // orphanRemoval supprime la ligne
            }
            Produit p = em.find(Produit.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    // ---- catégories ----
    public List<Categorie> findCategories() {
        EntityManager em = JPAUtil.em();
        try { return em.createQuery("SELECT c FROM Categorie c ORDER BY c.nom", Categorie.class).getResultList(); }
        finally { em.close(); }
    }

    public Categorie findCategorie(Long id) {
        EntityManager em = JPAUtil.em();
        try { return em.find(Categorie.class, id); } finally { em.close(); }
    }

    public long count() {
        EntityManager em = JPAUtil.em();
        try { return em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class).getSingleResult(); }
        finally { em.close(); }
    }
}
