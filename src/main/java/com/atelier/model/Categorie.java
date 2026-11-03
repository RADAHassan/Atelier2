package com.atelier.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "categorie")
public class Categorie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    public Categorie() {}
    public Categorie(String nom) { this.nom = nom; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
