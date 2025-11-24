package algo;

import algo.Hierholzer;
import modele.Graphe;
import modele.Sommet;
import modele.Arete;
import java.util.*;

// Implémente l'algorithme du Postier Chinois pour les graphes non orientés
// Résout le problème de trouver le circuit le plus court qui traverse chaque arête au moins une fois

public class PostierChinois {
    private Graphe graphe;
    private modele.GraphEuler graphEuler;

    public PostierChinois(Graphe graphe) {
        this.graphe = graphe;
        this.graphEuler = new modele.GraphEuler(graphe);
    }

    // Trouve le circuit optimal pour le Postier Chinois
    public List<Sommet> trouverCircuit() {
        System.out.println("\nALGORITHME DU POSTIER CHINOIS");

        // Vérification que le graphe n'est pas vide
        if (graphe.getSommets().isEmpty()) {
            System.out.println("Erreur : le graphe est vide !");
            return new ArrayList<>();
        }

        // Étape 1: Identifier les sommets de degré impair
        List<Sommet> sommetsImpairs = graphEuler.getSommetsImpairs();
        //System.out.println("• Sommets de degré impair identifiés : " + sommetsImpairs.size());

        // Cas particuliers
        if (sommetsImpairs.isEmpty()) {
            System.out.println("Cas idéal : tous les sommets sont de degré pair");
            System.out.println("Utilisation directe de l'algorithme d'Hierholzer");
            return resoudreCasIdeal();
        }

        if (sommetsImpairs.size() == 2) {
            System.out.println("Cas 2 sommets impairs : " + sommetsImpairs);
            System.out.println("Utilisation du chemin eulérien");
            return resoudreCasDeuxImpairs(sommetsImpairs);
        }

        // Cas général : nombre pair de sommets impairs (toujours le cas dans un graphe non orienté)
        System.out.println("Cas général : " + sommetsImpairs.size() + " sommets impairs");
        return resoudreCasGeneral(sommetsImpairs);
    }

    // Cas idéal : tous les sommets sont de degré pair
    // On utilise directement l'algorithme d'Hierholzer
    private List<Sommet> resoudreCasIdeal() {
        algo.Hierholzer hierholzer = new algo.Hierholzer(graphe);
        // Prendre le premier sommet comme départ (le dépôt P si possible)
        Sommet depart = trouverDepartOptimal();
        return hierholzer.trouverCycleEulerien(depart);
    }

    // Cas avec exactement 2 sommets impairs
    // On duplique le plus court chemin entre eux
    private List<Sommet> resoudreCasDeuxImpairs(List<Sommet> impairs) {
        Sommet s1 = impairs.get(0);
        Sommet s2 = impairs.get(1);

        /* DEBUG
        System.out.println("Calcul du plus court chemin entre " + s1 + " et " + s2);
        */
        List<Sommet> cheminMinimal = algo.Dijkstra.calculerChemin(graphe, s1, s2);
        /* DEBUG
        int distanceChemin = algo.Dijkstra.calculerDistanceChemin(graphe, cheminMinimal);
        System.out.println("Plus court chemin : " + cheminMinimal + " (distance: " + distanceChemin + "m)");
        System.out.println("Duplication des arêtes du chemin minimal");
        */

        Graphe grapheModifie = dupliquerAretesChemin(cheminMinimal);

        Hierholzer hierholzer = new algo.Hierholzer(grapheModifie);
        Sommet depart = trouverDepartOptimal();
        return hierholzer.trouverCycleEulerien(depart);
    }

    // Cas général : plus de 2 sommets impairs
    // On doit résoudre un appariement parfait de poids minimal
    private List<Sommet> resoudreCasGeneral(List<Sommet> impairs) {
        /* DEBUG
        System.out.println("Phase 1: Calcul des plus courts chemins entre paires de sommets impairs");
        */

        // Étape 1: Calculer la matrice des distances entre tous les sommets impairs
        int[][] matriceDistances = calculerMatriceDistances(impairs);

        /* DEBUG
        System.out.println("Phase 2: Recherche de l'appariement parfait de poids minimal");
        */
        List<Paire> appariementOptimal = trouverAppariementParfait(impairs, matriceDistances);

        /* DEBUG
        System.out.println("Phase 3: Duplication des arêtes des chemins sélectionnés");
        */
        Graphe grapheModifie = dupliquerAretesAppariement(appariementOptimal);

        /* DEBUG
        System.out.println("Phase 4: Application d'Hierholzer sur le graphe modifié");
        */
        Hierholzer hierholzer = new Hierholzer(grapheModifie);
        Sommet depart = trouverDepartOptimal();

        List<Sommet> circuit = hierholzer.trouverCycleEulerien(depart);

        // Calcul des statistiques
        calculerStatistiques(appariementOptimal);

        return circuit;
    }

    // Calcule la matrice des distances entre tous les sommets impairs
    private int[][] calculerMatriceDistances(List<Sommet> impairs) {
        int n = impairs.size();
        int[][] matrice = new int[n][n];

        /* DEBUG
        System.out.println("  Calcul des distances pour " + n + " sommets impairs...");
        */

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = algo.Dijkstra.calculerDistanceChemin(
                        graphe,
                        algo.Dijkstra.calculerChemin(graphe, impairs.get(i), impairs.get(j))
                );
                matrice[i][j] = distance;
                matrice[j][i] = distance;

                /* DEBUG
                System.out.println("    " + impairs.get(i) + " ↔ " + impairs.get(j) + " : " + distance + "m");
                */
            }
        }

        return matrice;
    }

    // Trouve l'appariement parfait de poids minimal (implémentation simplifiée)
    private List<Paire> trouverAppariementParfait(List<Sommet> impairs, int[][] matriceDistances) {
        int n = impairs.size();
        /* DEBUG
        System.out.println("  Recherche de l'appariement optimal pour " + n + " sommets");
        */

        // Implémentation simplifiée : algorithme glouton
        List<Paire> appariement = new ArrayList<>();
        boolean[] apparies = new boolean[n];

        // Tant qu'il reste des sommets non appariés
        while (true) {
            int iMin = -1, jMin = -1;
            int distanceMin = Integer.MAX_VALUE;

            // Trouver la paire non appariée avec la distance minimale
            for (int i = 0; i < n; i++) {
                if (!apparies[i]) {
                    for (int j = i + 1; j < n; j++) {
                        if (!apparies[j] && matriceDistances[i][j] < distanceMin) {
                            distanceMin = matriceDistances[i][j];
                            iMin = i;
                            jMin = j;
                        }
                    }
                }
            }

            if (iMin == -1) break; // Tous les sommets sont appariés

            // Ajouter cette paire à l'appariement
            appariement.add(new Paire(impairs.get(iMin), impairs.get(jMin), distanceMin));
            apparies[iMin] = true;
            apparies[jMin] = true;

            /* DEBUG
            System.out.println("    Appariement : " + impairs.get(iMin) + " ↔ " + impairs.get(jMin) + " (" + distanceMin + "m)");
            */
        }

        /* DEBUG
        System.out.println("Appariement trouvé : " + appariement.size() + " paires");
        */
        return appariement;
    }

    // Duplique les arêtes des chemins sélectionnés dans l'appariement
    private Graphe dupliquerAretesAppariement(List<Paire> appariement) {
        // Créer une copie du graphe original
        Graphe grapheModifie = copierGraphe();

        // Pour chaque paire dans l'appariement, dupliquer les arêtes du plus court chemin
        for (Paire paire : appariement) {
            List<Sommet> chemin = algo.Dijkstra.calculerChemin(graphe, paire.s1, paire.s2);
            /* DEBUG
            System.out.println("  Duplication du chemin: " + chemin + " (distance: " + paire.distance + "m)");
            */

            // Dupliquer chaque arête du chemin
            for (int i = 0; i < chemin.size() - 1; i++) {
                Sommet u = chemin.get(i);
                Sommet v = chemin.get(i + 1);
                int distance = graphe.getDistanceEntre(u, v);
                String nomRue = graphe.getNomRueEntre(u, v);

                // Ajouter une arête dupliquée
                grapheModifie.ajouterArete(u.getNom(), v.getNom(), distance, nomRue + "_dupliquee");
            }
        }

        return grapheModifie;
    }

    // Duplique les arêtes d'un chemin spécifique
    private Graphe dupliquerAretesChemin(List<Sommet> chemin) {
        Graphe grapheModifie = copierGraphe();

        for (int i = 0; i < chemin.size() - 1; i++) {
            Sommet u = chemin.get(i);
            Sommet v = chemin.get(i + 1);
            int distance = graphe.getDistanceEntre(u, v);
            String nomRue = graphe.getNomRueEntre(u, v);

            grapheModifie.ajouterArete(u.getNom(), v.getNom(), distance, nomRue + "_dupliquee");
        }

        return grapheModifie;
    }

    // Crée une copie complète du graphe original
    private Graphe copierGraphe() {
        Graphe copie = new Graphe();

        // Recréer toutes les arêtes originales
        for (Arete arete : graphe.getAretes()) {
            copie.ajouterArete(
                    arete.getDebut().getNom(),
                    arete.getFin().getNom(),
                    arete.getDistance(),
                    arete.getNomRue()
            );
        }

        return copie;
    }

    // Trouve le point de départ optimal (priorité au dépôt P)
    private Sommet trouverDepartOptimal() {
        // Essayer de trouver le dépôt P
        Sommet depot = graphe.trouverSommet("P");
        if (depot != null) {
            System.out.println("Point de départ : Dépôt P");
            return depot;
        }

        // Sinon prendre le premier sommet disponible
        if (!graphe.getSommets().isEmpty()) {
            Sommet premier = graphe.getSommets().get(0);
            System.out.println("Point de départ : " + premier + " (dépôt non trouvé)");
            return premier;
        }

        return null;
    }

    // Calcule et affiche les statistiques de l'appariement
    private void calculerStatistiques(List<Paire> appariement) {
        int distanceTotaleAjoutee = 0;
        for (Paire paire : appariement) {
            distanceTotaleAjoutee += paire.distance;
        }

        System.out.println("\nSTATISTIQUES DE L'APPARIEMENT:");
        System.out.println("  Distance totale ajoutée : " + distanceTotaleAjoutee + "m");
        System.out.println("  Nombre de paires : " + appariement.size());
        System.out.println("  Distance moyenne par paire : " +
                (appariement.isEmpty() ? 0 : distanceTotaleAjoutee / appariement.size()) + "m");
    }

    // Classe interne pour représenter une paire de sommets dans l'appariement
    private static class Paire {
        Sommet s1, s2;
        int distance;

        Paire(Sommet s1, Sommet s2, int distance) {
            this.s1 = s1;
            this.s2 = s2;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return s1 + "-" + s2 + "(" + distance + "m)";
        }
    }
}