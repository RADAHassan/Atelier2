package com.atelier.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "produit")
public class Produit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String designation;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false)
    private double prix;
    private int stock;
    private String image;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Produit() {}
    public Produit(String designation, String description, double prix, int stock, String image, Categorie categorie) {
        this.designation = designation; this.description = description; this.prix = prix;
        this.stock = stock; this.image = image; this.categorie = categorie;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
}
