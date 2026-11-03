package com.atelier.util;

import com.atelier.dao.ProduitDao;
import com.atelier.model.Categorie;
import com.atelier.model.Produit;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Initialise JPA au démarrage (création des tables) et insère un jeu de données si la base est vide. */
@WebListener
public class InitDataListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JPAUtil.getEmf(); // déclenche la génération du schéma
        if (new ProduitDao().count() > 0) return;
        EntityManager em = JPAUtil.em();
        try {
            em.getTransaction().begin();
            Categorie info = new Categorie("Informatique");
            Categorie tel = new Categorie("Téléphonie");
            Categorie acc = new Categorie("Accessoires");
            em.persist(info); em.persist(tel); em.persist(acc);
            em.persist(new Produit("Laptop 15 pouces", "PC portable 16 Go RAM, SSD 512 Go", 7499.0, 10, null, info));
            em.persist(new Produit("Écran 27 pouces 4K", "Dalle IPS 4K, HDMI/DisplayPort", 2899.0, 15, null, info));
            em.persist(new Produit("Smartphone X", "6,5 pouces, 128 Go, double SIM", 3990.0, 25, null, tel));
            em.persist(new Produit("Écouteurs sans fil", "Bluetooth 5.3, réduction de bruit", 599.0, 50, null, acc));
            em.persist(new Produit("Clavier mécanique", "Switchs rouges, rétroéclairé", 499.0, 30, null, acc));
            em.persist(new Produit("Souris sans fil", "Ergonomique, 2400 DPI", 149.0, 40, null, acc));
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { JPAUtil.close(); }
}
