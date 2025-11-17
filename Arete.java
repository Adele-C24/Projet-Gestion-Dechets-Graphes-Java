package modele;

public class Arete {
    private Sommet debut;
    private Sommet fin;
    private int distance;

    public Arete(Sommet debut, Sommet fin, int distance) {



        this.debut = debut;
        this.fin = fin;
        this.distance = distance;
    }

    // Getters
    public Sommet getDebut() { return debut; }
    public Sommet getFin() { return fin; }
    public int getDistance() { return distance; }

    @Override
    public String toString() {
        return debut + "-" + fin + "-" + distance;
    }
}