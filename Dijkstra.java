package algo;

import modele.Graphe;
import modele.Sommet;
import java.util.*;

public class Dijkstra {

    public static List<Sommet> calculerChemin(Graphe graphe, Sommet depart, Sommet arrivee) {
        if (depart == null || arrivee == null) {
            System.out.println("Erreur : sommet de départ ou d'arrivée null");
            return new ArrayList<>();
        }

        Map<Sommet, Integer> distances = new HashMap<>();
        Map<Sommet, Sommet> predecesseurs = new HashMap<>();
        Set<Sommet> visites = new HashSet<>();

        // Initialisation
        for (Sommet sommet : graphe.getSommets()) {
            distances.put(sommet, Integer.MAX_VALUE);
            predecesseurs.put(sommet, null);
        }
        distances.put(depart, 0);

        // Algorithme principal
        while (visites.size() < graphe.getSommets().size()) {
            Sommet courant = null;
            int minDistance = Integer.MAX_VALUE;

            for (Sommet sommet : graphe.getSommets()) {
                if (!visites.contains(sommet) && distances.get(sommet) < minDistance) {
                    courant = sommet;
                    minDistance = distances.get(sommet);
                }
            }

            if (courant == null) break;
            visites.add(courant);

            if (courant.equals(arrivee)) {
                break;
            }

            // Mettre à jour les voisins
            for (modele.Arete arete : graphe.getAretes()) {
                Sommet voisin = null;
                if (arete.getDebut().equals(courant)) {
                    voisin = arete.getFin();
                } else if (arete.getFin().equals(courant)) {
                    voisin = arete.getDebut();
                }

                if (voisin != null && !visites.contains(voisin)) {
                    int nouvelleDistance = distances.get(courant) + arete.getDistance();
                    if (nouvelleDistance < distances.get(voisin)) {
                        distances.put(voisin, nouvelleDistance);
                        predecesseurs.put(voisin, courant);
                    }
                }
            }
        }

        return reconstruireChemin(predecesseurs, depart, arrivee);
    }

    private static List<Sommet> reconstruireChemin(Map<Sommet, Sommet> predecesseurs,
                                                   Sommet depart, Sommet arrivee) {
        List<Sommet> chemin = new ArrayList<>();
        Sommet courant = arrivee;

        while (courant != null) {
            chemin.add(0, courant);
            courant = predecesseurs.get(courant);
        }

        if (!chemin.isEmpty() && chemin.get(0).equals(depart)) {
            return chemin;
        } else {
            System.out.println("Aucun chemin trouvé entre " + depart + " et " + arrivee);
            return new ArrayList<>();
        }
    }

    public static int calculerDistanceChemin(Graphe graphe, List<Sommet> chemin) {
        int distance = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            distance += graphe.getDistanceEntre(chemin.get(i), chemin.get(i+1));
        }
        return distance;
    }
}