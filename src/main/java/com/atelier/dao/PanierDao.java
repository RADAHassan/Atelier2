package com.atelier.dao;

import com.atelier.model.Internaute;
import com.atelier.model.LignePanier;
import com.atelier.model.Panier;
import com.atelier.model.Produit;
import com.atelier.util.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;

public class PanierDao {

    @FunctionalInterface
    private interface Work<T> { T run(EntityManager em); }

    private <T> T tx(Work<T> w) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            T r = w.run(em);
            em.getTransaction().commit();
            return r;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    public Panier creer(Internaute internaute) {
        return tx(em -> {
            Panier p = new Panier();
            if (internaute != null) p.setInternaute(em.find(Internaute.class, internaute.getId()));
            em.persist(p);
            return p;
        });
    }

    public Panier find(Long id) {
        if (id == null) return null;
        EntityManager em = JPAUtil.em();
        try { return em.find(Panier.class, id); } finally { em.close(); }
    }

    /** Dernier panier d'un internaute (ou null). */
    public Panier findByInternaute(Internaute i) {
        EntityManager em = JPAUtil.em();
        try {
            List<Panier> r = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :i ORDER BY p.id DESC", Panier.class)
                    .setParameter("i", i.getId()).setMaxResults(1).getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally { em.close(); }
    }

    public Panier rattacher(Long panierId, Internaute i) {
        return tx(em -> {
            Panier p = em.find(Panier.class, panierId);
            p.setInternaute(em.find(Internaute.class, i.getId()));
            return p;
        });
    }

    public Panier ajouter(Long panierId, Long produitId, int qte) {
        return tx(em -> {
            Panier p = em.find(Panier.class, panierId);
            Produit prod = em.find(Produit.class, produitId);
            if (prod == null || qte <= 0) return p;
            for (LignePanier l : p.getLignes()) {
                if (l.getProduit().getId().equals(produitId)) {
                    l.setQuantite(Math.min(prod.getStock(), l.getQuantite() + qte));
                    return p;
                }
            }
            p.getLignes().add(new LignePanier(p, prod, Math.min(prod.getStock(), qte)));
            return p;
        });
    }

    public Panier modifierQuantite(Long panierId, Long produitId, int qte) {
        return tx(em -> {
            Panier p = em.find(Panier.class, panierId);
            p.getLignes().removeIf(l -> {
                if (!l.getProduit().getId().equals(produitId)) return false;
                if (qte <= 0) return true;
                l.setQuantite(Math.min(l.getProduit().getStock(), qte));
                return false;
            });
            return p;
        });
    }

    public Panier retirer(Long panierId, Long produitId) {
        return modifierQuantite(panierId, produitId, 0);
    }

    public Panier vider(Long panierId) {
        return tx(em -> {
            Panier p = em.find(Panier.class, panierId);
            p.getLignes().clear();
            return p;
        });
    }
}
