package com.atelier.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ligne_panier")
public class LignePanier implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "panier_id", nullable = false)
    private Panier panier;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
    private int quantite;

    public LignePanier() {}
    public LignePanier(Panier panier, Produit produit, int quantite) {
        this.panier = panier; this.produit = produit; this.quantite = quantite;
    }

    public double getSousTotal() { return produit.getPrix() * quantite; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Panier getPanier() { return panier; }
    public void setPanier(Panier panier) { this.panier = panier; }
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
