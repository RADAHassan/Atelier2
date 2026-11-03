package com.atelier.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "internaute")
public class Internaute implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String nom;
    @Column(nullable = false, length = 80)
    private String prenom;
    @Column(nullable = false, unique = true, length = 120)
    private String email;
    @Column(nullable = false, length = 64)
    private String motDePasse; // SHA-256 hex

    public Internaute() {}
    public Internaute(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom; this.prenom = prenom; this.email = email; this.motDePasse = motDePasse;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
