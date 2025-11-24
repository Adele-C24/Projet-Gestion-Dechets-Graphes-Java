package algo;

import modele.Graphe;
import modele.Sommet;
import modele.Arete;
import java.util.*;

// Implémente l'algorithme d'Hierholzer pour trouver un cycle eulérien
public class Hierholzer {
    private Graphe graphe;

    public Hierholzer(Graphe graphe) {
        this.graphe = graphe;
    }

    // Trouve un cycle eulérien dans le graphe
    public List<Sommet> trouverCycleEulerien(Sommet depart) {
        /* DEBUG
        System.out.println("\n Début de l'algorithme d'Hierholzer");
        System.out.println("Point de départ: " + depart);
        */

        // Vérification préalable
        if (graphe.getSommets().isEmpty()) {
            return new ArrayList<>();
        }

        // Construire le graphe résiduel (copie modifiable des arêtes)
        Map<Sommet, LinkedList<Sommet>> grapheResiduel = construireGrapheResiduel();

        if (grapheResiduel.isEmpty()) {
            return new ArrayList<>();
        }

        // Structures pour l'algorithme
        Stack<Sommet> pile = new Stack<>();
        LinkedList<Sommet> circuit = new LinkedList<>();

        // Étape 1: Empiler le sommet de départ
        pile.push(depart);
        /* DEBUG
        System.out.println(" Sommet départ empilé: " + depart);
        */

        // Étape 2: Algorithme principal
        int iterations = 0;
        while (!pile.isEmpty()) {
            iterations++;
            Sommet courant = pile.peek();

            // Vérifier s'il reste des arêtes à parcourir depuis ce sommet
            if (grapheResiduel.containsKey(courant) && !grapheResiduel.get(courant).isEmpty()) {
                // Prendre le premier voisin disponible
                Sommet voisin = grapheResiduel.get(courant).removeFirst();

                // Retirer l'arête inverse (graphe non orienté)
                retirerAreteInverse(grapheResiduel, courant, voisin);

                // Empiler le voisin
                pile.push(voisin);

                /* DEBUG
                System.out.println("  > De " + courant + " à " + voisin + " (itération " + iterations + ")");
                */
            } else {
                // Aucun voisin disponible, ajouter au circuit
                Sommet retire = pile.pop();
                circuit.addFirst(retire); // Ajouter au début pour l'ordre inverse

                /* DEBUG
                System.out.println(" Retrait de la pile: " + retire);
                */
            }
        }

        /* DEBUG
        System.out.println(" Algorithme terminé en " + iterations + " itérations");
        System.out.println("Cycle eulérien trouvé avec " + circuit.size() + " étapes");
        */

        return circuit;
    }

    // Construit une représentation modifiable du graphe
    private Map<Sommet, LinkedList<Sommet>> construireGrapheResiduel() {
        Map<Sommet, LinkedList<Sommet>> grapheResiduel = new HashMap<>();

        // Pour chaque arête, ajouter la connexion dans les deux sens
        for (Arete arete : graphe.getAretes()) {
            ajouterConnexion(grapheResiduel, arete.getDebut(), arete.getFin());
            ajouterConnexion(grapheResiduel, arete.getFin(), arete.getDebut());
        }

        /* DEBUG
        System.out.println(" Graphe résiduel construit: " + grapheResiduel.size() + " sommets actifs");
        */
        return grapheResiduel;
    }

    // Ajoute une connexion entre deux sommets dans le graphe résiduel
    private void ajouterConnexion(Map<Sommet, LinkedList<Sommet>> graphe, Sommet u, Sommet v) {
        graphe.computeIfAbsent(u, k -> new LinkedList<>()).add(v);
    }

    // Retire une arête dans les deux sens (graphe non orienté)
    private void retirerAreteInverse(Map<Sommet, LinkedList<Sommet>> graphe, Sommet u, Sommet v) {
        if (graphe.containsKey(v)) {
            graphe.get(v).remove(u);
            if (graphe.get(v).isEmpty()) {
                graphe.remove(v);
            }
        }
    }

    // Calcule la distance totale d'un circuit
    public int calculerDistanceCircuit(List<Sommet> circuit) {
        if (circuit == null || circuit.size() < 2) {
            return 0;
        }

        int distance = 0;
        for (int i = 0; i < circuit.size() - 1; i++) {
            int segment = graphe.getDistanceEntre(circuit.get(i), circuit.get(i + 1));
            if (segment > 0) {
                distance += segment;
            }
        }
        return distance;
    }
}