package com.atelier.dao;

import com.atelier.model.Internaute;
import com.atelier.util.JPAUtil;
import java.util.List;
import javax.persistence.EntityManager;

public class InternauteDao {

    public Internaute inscrire(Internaute i) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            em.persist(i);
            em.getTransaction().commit();
            return i;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    public Internaute findByEmail(String email) {
        EntityManager em = JPAUtil.em();
        try {
            List<Internaute> r = em.createQuery("SELECT i FROM Internaute i WHERE i.email = :e", Internaute.class)
                    .setParameter("e", email).getResultList();
            return r.isEmpty() ? null : r.get(0);
        } finally { em.close(); }
    }

    public List<Internaute> findAll() {
        EntityManager em = JPAUtil.em();
        try { return em.createQuery("SELECT i FROM Internaute i ORDER BY i.nom", Internaute.class).getResultList(); }
        finally { em.close(); }
    }

    public void supprimer(Long id) {
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Internaute i = em.find(Internaute.class, id);
            if (i != null) {
                List<com.atelier.model.Panier> paniers = em
                        .createQuery("SELECT p FROM Panier p WHERE p.internaute = :i", com.atelier.model.Panier.class)
                        .setParameter("i", i).getResultList();
                for (com.atelier.model.Panier p : paniers) em.remove(p); // cascade sur les lignes
                em.remove(i);
            }
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }
}
