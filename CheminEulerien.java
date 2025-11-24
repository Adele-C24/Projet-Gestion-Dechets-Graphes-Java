package algo;

import modele.Graphe;
import modele.Sommet;
import modele.Arete;
import java.util.*;

// Implémente la recherche de chemin eulérien pour les graphes avec 2 sommets impairs
public class CheminEulerien {
    private Graphe graphe;
    private modele.GraphEuler graphEuler;

    public CheminEulerien(Graphe graphe) {
        this.graphe = graphe;
        this.graphEuler = new modele.GraphEuler(graphe);
    }

    // Trouve un chemin eulérien dans le graphe
    // Le chemin commence et se termine aux sommets de degré impair

    public List<Sommet> trouverCheminEulerien(Sommet depart) {
        System.out.println("\n DÉBUT RECHERCHE DE CHEMIN EULÉRIEN");

        // Vérification des conditions
        if (!graphEuler.admetCheminEulerien()) {
            System.out.println(" Conditions du chemin eulérien non satisfaites !");
            return new ArrayList<>();
        }

        List<Sommet> impairs = graphEuler.getSommetsImpairs();
        System.out.println("• Sommets impairs : " + impairs.get(0) + " et " + impairs.get(1));

        // Vérifier que le départ est bien un sommet impair
        if (!impairs.contains(depart)) {
            System.out.println(" Attention : le point de départ " + depart + " n'est pas un sommet impair");
            System.out.println(" Les sommets impairs sont : " + impairs);
        }

        // Dupliquer le plus court chemin entre les deux sommets impairs
        Sommet autreImpair = impairs.get(0).equals(depart) ? impairs.get(1) : impairs.get(0);
        System.out.println("• Calcul du plus court chemin vers l'autre sommet impair : " + autreImpair);

        List<Sommet> cheminMinimal = algo.Dijkstra.calculerChemin(graphe, depart, autreImpair);
        int distanceChemin = algo.Dijkstra.calculerDistanceChemin(graphe, cheminMinimal);
        System.out.println("✓ Plus court chemin : " + cheminMinimal + " (distance: " + distanceChemin + "m)");

        // Dupliquer les arêtes du chemin minimal
        System.out.println("• Duplication des arêtes du chemin minimal");
        Graphe grapheModifie = dupliquerAretesChemin(cheminMinimal);

        // Appliquer Hierholzer sur le graphe modifié
        algo.Hierholzer hierholzer = new algo.Hierholzer(grapheModifie);
        List<Sommet> circuit = hierholzer.trouverCycleEulerien(depart);

        System.out.println(" Chemin eulérien trouvé avec " + circuit.size() + " étapes");
        System.out.println(" Distance ajoutée par duplication : " + distanceChemin + "m");

        return circuit;
    }

    // Duplique les arêtes d'un chemin pour rendre le graphe eulérien
    private Graphe dupliquerAretesChemin(List<Sommet> chemin) {
        // Créer une copie du graphe original
        Graphe grapheModifie = new Graphe();

        // Recréer toutes les arêtes originales
        for (Arete arete : graphe.getAretes()) {
            grapheModifie.ajouterArete(
                    arete.getDebut().getNom(),
                    arete.getFin().getNom(),
                    arete.getDistance(),
                    arete.getNomRue()
            );
        }

        // Dupliquer chaque arête du chemin
        for (int i = 0; i < chemin.size() - 1; i++) {
            Sommet u = chemin.get(i);
            Sommet v = chemin.get(i + 1);
            int distance = graphe.getDistanceEntre(u, v);
            String nomRue = graphe.getNomRueEntre(u, v);

            // Ajouter une arête dupliquée
            grapheModifie.ajouterArete(u.getNom(), v.getNom(), distance, nomRue + "_dupliquee");

            System.out.println("-> Duplication: " + u + " → " + v + " via " + nomRue + " (" + distance + "m)");
        }

        return grapheModifie;
    }
}