package com.atelier.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/** Fabrique unique d'EntityManager (une EMF pour toute l'application). */
public final class JPAUtil {
    private static EntityManagerFactory emf;

    private JPAUtil() {}

    public static synchronized EntityManagerFactory getEmf() {
        if (emf == null || !emf.isOpen()) emf = Persistence.createEntityManagerFactory("ecommercePU");
        return emf;
    }
    public static EntityManager em() { return getEmf().createEntityManager(); }
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
