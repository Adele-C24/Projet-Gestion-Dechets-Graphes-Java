package modele;

//Représente un sommet dans le graphe
//Chaque sommet a un nom unique identifiant le carrefour

public class Sommet {
    private String nom;

    public Sommet(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sommet sommet = (Sommet) obj;
        return nom.equals(sommet.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    @Override
    public String toString() {
        return nom;
    }
}
