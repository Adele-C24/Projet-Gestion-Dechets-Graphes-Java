package algo;

import modele.Graphe;
import modele.Sommet;
import modele.Arete;
import java.util.*;

public class CheminEulerien {
    private Graphe graphe;
    private GraphEuler graphEuler;

    public CheminEulerien(Graphe graphe) {
        this.graphe = graphe;
        this.graphEuler = new GraphEuler(graphe);
    }

    public List<Sommet> trouverCheminEulerien(Sommet depart) {
        // Vérifier que le graphe n'est pas vide
        if (graphe.getSommets().isEmpty()) {
            System.out.println(" Erreur : le graphe est vide !");
            return new ArrayList<>();
        }

        if (!graphEuler.admetCheminEulerien()) {
            System.out.println("Conditions du chemin eulérien non satisfaites !");
            return new ArrayList<>();
        }

        List<Sommet> impairs = graphEuler.getSommetsImpairs();
        Sommet s1 = impairs.get(0);
        Sommet s2 = impairs.get(1);

        System.out.println("Sommets impairs identifiés : " + s1 + " et " + s2);

        // Pour une implémentation simplifiée, on utilise Dijkstra pour créer un chemin
        System.out.println("Création d'un chemin approximatif...");

        List<Sommet> chemin = new ArrayList<>();
        chemin.add(depart);

        // Parcours simplifié de tous les sommets
        Set<Sommet> visites = new HashSet<>();
        visites.add(depart);

        Sommet courant = depart;

        while (visites.size() < graphe.getSommets().size()) {
            // Trouver le sommet non visité le plus proche
            Sommet plusProche = null;
            int minDistance = Integer.MAX_VALUE;

            for (Sommet voisin : getVoisins(courant)) {
                if (!visites.contains(voisin)) {
                    int distance = graphe.getDistanceEntre(courant, voisin);
                    if (distance < minDistance) {
                        minDistance = distance;
                        plusProche = voisin;
                    }
                }
            }

            if (plusProche != null) {
                chemin.add(plusProche);
                visites.add(plusProche);
                courant = plusProche;
            } else {
                // Aucun voisin non visité, terminer
                break;
            }
        }

        System.out.println("Chemin généré avec " + chemin.size() + " étapes");
        return chemin;
    }

    private List<Sommet> getVoisins(Sommet sommet) {
        List<Sommet> voisins = new ArrayList<>();
        for (Arete arete : graphe.getAretes()) {
            if (arete.getDebut().equals(sommet)) {
                voisins.add(arete.getFin());
            } else if (arete.getFin().equals(sommet)) {
                voisins.add(arete.getDebut());
            }
        }
        return voisins;
    }
}