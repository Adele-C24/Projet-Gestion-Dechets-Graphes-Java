package algo;

import modele.Graphe;
import modele.Sommet;
import java.util.*;




public class PlusProcheVoisin {

    public static List<Sommet> calculerTournee(Graphe graphe, Sommet depot, List<Sommet> points) {
        List<Sommet> tournee = new ArrayList<>();
        List<Sommet> pointsRestants = new ArrayList<>(points);

        tournee.add(depot);
        Sommet courant = depot;

        while (!pointsRestants.isEmpty()) {
            Sommet plusProche = null;
            int distanceMin = Integer.MAX_VALUE;

            for (Sommet point : pointsRestants) {
                List<Sommet> chemin = Dijkstra.calculerChemin(graphe, courant, point);
                if (!chemin.isEmpty()) {
                    int distance = Dijkstra.calculerDistanceChemin(graphe, chemin);
                    if (distance < distanceMin) {
                        distanceMin = distance;
                        plusProche = point;
                    }
                }
            }

            if (plusProche != null) {
                List<Sommet> cheminVersPlusProche = Dijkstra.calculerChemin(graphe, courant, plusProche);
                if (cheminVersPlusProche.size() > 1) {
                    for (int i = 1; i < cheminVersPlusProche.size(); i++) {
                        tournee.add(cheminVersPlusProche.get(i));
                    }
                }
                pointsRestants.remove(plusProche);
                courant = plusProche;
            } else {
                System.out.println(" Impossible de trouver un chemin vers les points restants");
                break;
            }
        }

        // Retour au dépôt
        List<Sommet> cheminRetour = Dijkstra.calculerChemin(graphe, courant, depot);
        if (cheminRetour.size() > 1) {
            for (int i = 1; i < cheminRetour.size(); i++) {
                tournee.add(cheminRetour.get(i));
            }
        }

        return tournee;
    }
}