package modele;

// Représente une arête (rue) dans le graphe
// Relie deux sommets et possède une distance et un nom de rue

public class Arete {
    private Sommet debut;
    private Sommet fin;
    private int distance;
    private String nomRue;

    public Arete(Sommet debut, Sommet fin, int distance, String nomRue) {
        this.debut = debut;
        this.fin = fin;
        this.distance = distance;
        this.nomRue = nomRue;
    }

    // Getters
    public Sommet getDebut() { return debut; }
    public Sommet getFin() { return fin; }
    public int getDistance() { return distance; }
    public String getNomRue() { return nomRue; }

    @Override
    public String toString() {
        return debut + "-" + fin + " (" + nomRue + ") : " + distance + "m";
    }
}