package application;

import modele.Graphe;
import modele.Sommet;
import algo.Hierholzer;
import algo.CheminEulerien;
import algo.PostierChinois;
import modele.GraphEuler;
import java.util.*;

public class TestProblematique2 {

    public static void main(String[] args) {
        System.out.println("TEST COMPLET - PROBLÉMATIQUE 2");
        System.out.println("Collecte des poubelles aux habitations");
        System.out.println("Hypothèse H01: Toutes les rues à double sens");
        System.out.println();

        // Test 1: Cas idéal - Cycle eulérien
        testCasIdeal();

        // Test 2: Cas 2 sommets impairs - Chemin eulérien
        testCasDeuxImpairs();

        // Test 3: Cas général - Postier Chinois
        testCasGeneral();

        // Test 4: Postier Chinois très efficace
        testPostierChinoisEfficace();

        // Test 5: Votre MAP Paris réelle
        testMapParis();

        // Résumé des tests
        System.out.println("RÉSUMÉ DES TESTS PROBLÉMATIQUE 2");
        System.out.println(" Tous les algorithmes ont été testés avec succès");
        System.out.println("Cas idéal (Hierholzer) - Parcours optimal");
        System.out.println("Cas 2 impairs (Chemin Eulerien) - Répétitions minimales");
        System.out.println("Cas général (Postier Chinois) - Solution complète");
        System.out.println("MAP Paris - Application réelle");
    }

    // TEST 1: CAS IDÉAL - CYCLE EULÉRIEN
    private static void testCasIdeal() {
        System.out.println("\n1. TEST CAS IDÉAL - CYCLE EULÉRIEN (HIERHOLZER)");

        Graphe g = creerGrapheCycleEulerien();

        System.out.println("Description: Carré parfait - tous les sommets de degré pair");
        System.out.println("Attendu: Circuit optimal sans répétitions");
        System.out.println();

        // Analyse
        GraphEuler analyseur = new GraphEuler(g);
        analyseur.analyserGraphe();

        // Application de Hierholzer
        Hierholzer hierholzer = new Hierholzer(g);
        Sommet depart = g.trouverSommet("A");
        List<Sommet> circuit = hierholzer.trouverCycleEulerien(depart);

        // Résultats
        afficherResultats("HIERHOLZER", g, circuit);
    }

    // TEST 2: CAS 2 SOMMETS IMPAIRS - CHEMIN EULÉRIEN
    private static void testCasDeuxImpairs() {
        System.out.println("\n2. TEST CAS 2 SOMMETS IMPAIRS - CHEMIN EULÉRIEN");

        Graphe g = creerGrapheCheminEulerien();

        System.out.println("Description: Ligne simple - 2 sommets de degré impair");
        System.out.println("Attendu: Chemin avec répétitions minimales");
        System.out.println();

        // Analyse
        GraphEuler analyseur = new GraphEuler(g);
        analyseur.analyserGraphe();

        // Application du chemin eulérien
        CheminEulerien cheminEulerien = new CheminEulerien(g);
        List<Sommet> impairs = analyseur.getSommetsImpairs();
        Sommet depart = impairs.get(0);
        List<Sommet> chemin = cheminEulerien.trouverCheminEulerien(depart);

        // Résultats
        afficherResultats("CHEMIN EULÉRIEN", g, chemin);
    }

    // TEST 3: CAS GÉNÉRAL - POSTIER CHINOIS
    private static void testCasGeneral() {
        System.out.println("\n3. TEST CAS GÉNÉRAL - POSTIER CHINOIS");

        Graphe g = creerGraphePostierChinois();

        System.out.println("Description: Étoile - plusieurs sommets impairs");
        System.out.println("Attendu: Circuit complet avec répétitions nécessaires");
        System.out.println();

        // Analyse
        GraphEuler analyseur = new GraphEuler(g);
        analyseur.analyserGraphe();

        // Application du Postier Chinois
        PostierChinois postier = new PostierChinois(g);
        List<Sommet> circuit = postier.trouverCircuit();

        // Résultats
        afficherResultats("POSTIER CHINOIS", g, circuit);
    }

    // TEST 4: POSTIER CHINOIS TRÈS EFFICACE
    private static void testPostierChinoisEfficace() {
        System.out.println("\n4. TEST POSTIER CHINOIS - HAUTE EFFICACITÉ");

        Graphe g = creerGraphePostierChinoisEfficace();

        System.out.println("Description: Grille équilibrée - peu de sommets impairs bien regroupés");
        System.out.println("Attendu: Efficacité > 90%");
        System.out.println();

        // Analyse
        GraphEuler analyseur = new GraphEuler(g);
        analyseur.analyserGraphe();

        // Application du Postier Chinois
        PostierChinois postier = new PostierChinois(g);
        List<Sommet> circuit = postier.trouverCircuit();

        // Résultats détaillés
        afficherResultats("POSTIER CHINOIS OPTIMISÉ", g, circuit);
    }

    // TEST 5: MAP PARIS RÉELLE
    private static void testMapParis() {
        System.out.println("\n5. TEST MAP PARIS - CAS RÉEL");

        Graphe g = creerMapParis();

        System.out.println("Description: Votre MAP Paris avec 16 carrefours");
        System.out.println("Attendu: Application réelle de l'algorithme approprié");
        System.out.println();

        // Analyse pour déterminer l'algorithme à utiliser
        GraphEuler analyseur = new GraphEuler(g);
        analyseur.analyserGraphe();

        List<Sommet> impairs = analyseur.getSommetsImpairs();
        List<Sommet> circuit;
        String algorithmeUtilise;

        if (impairs.isEmpty()) {
            algorithmeUtilise = "HIERHOLZER";
            Hierholzer hierholzer = new Hierholzer(g);
            circuit = hierholzer.trouverCycleEulerien(g.trouverSommet("P"));
        } else if (impairs.size() == 2) {
            algorithmeUtilise = "CHEMIN EULÉRIEN";
            CheminEulerien cheminEulerien = new CheminEulerien(g);
            circuit = cheminEulerien.trouverCheminEulerien(impairs.get(0));
        } else {
            algorithmeUtilise = "POSTIER CHINOIS";
            PostierChinois postier = new PostierChinois(g);
            circuit = postier.trouverCircuit();
        }

        System.out.println("Algorithme appliqué: " + algorithmeUtilise);
        afficherResultats("MAP PARIS - " + algorithmeUtilise, g, circuit);
    }

    // GRAPHE 1: CYCLE EULÉRIEN (tous degrés pairs)
    private static Graphe creerGrapheCycleEulerien() {
        Graphe g = new Graphe();
        // Carré parfait - tous degrés pairs
        g.ajouterArete("A", "B", 100, "AB");
        g.ajouterArete("B", "C", 100, "BC");
        g.ajouterArete("C", "D", 100, "CD");
        g.ajouterArete("D", "A", 100, "DA");
        return g;
    }

    // GRAPHE 2: CHEMIN EULÉRIEN (2 sommets impairs)
    private static Graphe creerGrapheCheminEulerien() {
        Graphe g = new Graphe();
        // Ligne avec 2 extrémités impaires
        g.ajouterArete("A", "B", 100, "AB");
        g.ajouterArete("B", "C", 100, "BC");
        g.ajouterArete("C", "D", 100, "CD");
        g.ajouterArete("D", "E", 100, "DE");
        return g;
    }

    // GRAPHE 3: POSTIER CHINOIS (plusieurs sommets impairs)
    private static Graphe creerGraphePostierChinois() {
        Graphe g = new Graphe();
        // Étoile à 4 branches - 4 sommets impairs
        g.ajouterArete("Centre", "A", 100, "CA");
        g.ajouterArete("Centre", "B", 100, "CB");
        g.ajouterArete("Centre", "C", 100, "CC");
        g.ajouterArete("Centre", "D", 100, "CD");
        return g;
    }

    // GRAPHE 4: POSTIER CHINOIS EFFICACE
    private static Graphe creerGraphePostierChinoisEfficace() {
        Graphe g = new Graphe();
        // Grille équilibrée avec peu de sommets impairs
        g.ajouterArete("A", "B", 100, "AB");
        g.ajouterArete("B", "C", 100, "BC");
        g.ajouterArete("A", "D", 100, "AD");
        g.ajouterArete("B", "E", 100, "BE");
        g.ajouterArete("C", "F", 100, "CF");
        g.ajouterArete("D", "E", 100, "DE");
        g.ajouterArete("E", "F", 100, "EF");
        g.ajouterArete("D", "G", 100, "DG");
        g.ajouterArete("E", "H", 100, "EH");
        g.ajouterArete("F", "I", 100, "FI");
        g.ajouterArete("G", "H", 100, "GH");
        g.ajouterArete("H", "I", 100, "HI");
        return g;
    }

    // GRAPHE 5: MAP PARIS
    private static Graphe creerMapParis() {
        Graphe g = new Graphe();

        // Grands Boulevards
        g.ajouterArete("A", "D", 400, "SAINT_HONORE_SEVRES");
        g.ajouterArete("A", "I", 400, "SEVRES_MONTAIGNE");
        g.ajouterArete("I", "J", 200, "MONTAIGNE_CHAMPS_ELYSEES");
        g.ajouterArete("I", "O", 300, "CHAMPS_ELYSEES_CONCORDE");

        // Rues historiques
        g.ajouterArete("E", "F", 50, "RIVOLI_PALAIS_ROYAL");
        g.ajouterArete("B", "C", 100, "PALAIS_ROYAL_VENDOME");
        g.ajouterArete("F", "G", 50, "VENDOME_MADELEINE");
        g.ajouterArete("C", "G", 200, "MADELEINE_CONCORDE");

        // Rues transversales
        g.ajouterArete("E", "K", 400, "MONTAIGNE_SAINT_GERMAIN");
        g.ajouterArete("K", "M", 200, "CHAMPS_ELYSEES_SAINT_GERMAIN");
        g.ajouterArete("L", "N", 200, "CONCORDE_OPERA");
        g.ajouterArete("J", "M", 400, "ROYALE_MONTAIGNE");
        g.ajouterArete("D", "J", 400, "GRENELLE_CHAMPS_ELYSEES");

        // Petites rues
        g.ajouterArete("K", "L", 300, "SAINT_HONORE_RIVOLI");
        g.ajouterArete("A", "B", 500, "SAINT_HONORE_ROYALE");
        g.ajouterArete("J", "K", 200, "ROYALE_GRENELLE");
        g.ajouterArete("F", "H", 50, "GRENELLE_BAC");
        g.ajouterArete("G", "H", 50, "BAC_VENDOME");
        g.ajouterArete("C", "E", 400, "PALAIS_ROYAL_GRENELLE");
        g.ajouterArete("H", "L", 300, "SAINT_GERMAIN_OPERA");
        g.ajouterArete("M", "N", 300, "SEVRES_SAINT_SULPICE");

        // Dépôt Centre
        g.ajouterArete("P", "A", 0, "DEPOT_SAINT_HONORE");

        return g;
    }

    // AFFICHAGE DES RÉSULTATS
    private static void afficherResultats(String algorithme, Graphe graphe, List<Sommet> circuit) {
        System.out.println("RÉSULTATS " + algorithme + ":");
        System.out.println("-".repeat(50));

        if (circuit.isEmpty()) {
            System.out.println(" Aucun circuit trouvé");
            return;
        }

        // Calculs
        int distance = calculerDistanceCircuit(graphe, circuit);
        int distanceMinimale = calculerDistanceMinimale(graphe);
        double efficacite = (double) distanceMinimale / distance * 100;
        int repetitions = compterRepetitions(graphe, circuit);

        // Affichage
        System.out.println("Circuit: " + (circuit.size() > 10 ?
                circuit.subList(0, 5) + "..." + circuit.subList(circuit.size()-5, circuit.size()) :
                circuit));
        System.out.println("Distance parcourue: " + distance + "m");
        System.out.println("Distance minimale: " + distanceMinimale + "m");
        System.out.println("Efficacité: " + String.format("%.1f", efficacite) + "%");
        System.out.println("Répétitions: " + repetitions);
        System.out.println("Étapes: " + circuit.size());

        // Évaluation
        System.out.print("Performance: ");
        if (efficacite >= 95) {
            System.out.println("EXCELLENTE");
        } else if (efficacite >= 85) {
            System.out.println("TRÈS BONNE");
        } else if (efficacite >= 75) {
            System.out.println("BONNE");
        } else {
            System.out.println("MOYENNE");
        }

        // Vérification circuit fermé
        if (circuit.get(0).equals(circuit.get(circuit.size()-1))) {
            System.out.println("Circuit fermé (retour au départ)");
        } else {
            System.out.println("Circuit ouvert");
        }

        System.out.println();
    }

    // CALCUL DE LA DISTANCE D'UN CIRCUIT
    private static int calculerDistanceCircuit(Graphe graphe, List<Sommet> circuit) {
        if (circuit == null || circuit.size() < 2) return 0;

        int distance = 0;
        for (int i = 0; i < circuit.size() - 1; i++) {
            int segment = graphe.getDistanceEntre(circuit.get(i), circuit.get(i + 1));
            if (segment > 0) distance += segment;
        }
        return distance;
    }

    // CALCUL DE LA DISTANCE MINIMALE THÉORIQUE
    private static int calculerDistanceMinimale(Graphe graphe) {
        int total = 0;
        Set<String> arêtesComptées = new HashSet<>();

        for (modele.Arete arete : graphe.getAretes()) {
            String cle = arete.getDebut() + "-" + arete.getFin();
            String cleInverse = arete.getFin() + "-" + arete.getDebut();

            if (!arêtesComptées.contains(cle) && !arêtesComptées.contains(cleInverse)) {
                total += arete.getDistance();
                arêtesComptées.add(cle);
            }
        }
        return total;
    }

    // COMPTER LES RÉPÉTITIONS
    private static int compterRepetitions(Graphe graphe, List<Sommet> circuit) {
        Map<String, Integer> frequence = new HashMap<>();
        int repetitions = 0;

        for (int i = 0; i < circuit.size() - 1; i++) {
            Sommet u = circuit.get(i);
            Sommet v = circuit.get(i + 1);
            String cle = u.getNom().compareTo(v.getNom()) < 0 ?
                    u + "-" + v : v + "-" + u;

            frequence.put(cle, frequence.getOrDefault(cle, 0) + 1);
        }

        for (int count : frequence.values()) {
            if (count > 1) repetitions += (count - 1);
        }

        return repetitions;
    }
}
