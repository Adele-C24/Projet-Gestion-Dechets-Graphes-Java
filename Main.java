package application;

import modele.Graphe;
import modele.Sommet;
import algo.Dijkstra;
import algo.PlusProcheVoisin;
import algo.Hierholzer;
import algo.CheminEulerien;
import algo.PostierChinois;
import modele.GraphEuler;
import java.util.*;

public class Main {

    private static Graphe creerGrapheManuellement() {
        Graphe graphe = new Graphe();

        // Ajouter manuellement les arêtes de votre MAP
        graphe.ajouterArete("A", "D", 400, "SAINT_HONORE_SEVRES");
        graphe.ajouterArete("A", "I", 400, "SEVRES_MONTAIGNE");
        graphe.ajouterArete("I", "J", 200, "MONTAIGNE_CHAMPS_ELYSEES");
        graphe.ajouterArete("I", "O", 300, "CHAMPS_ELYSEES_CONCORDE");
        graphe.ajouterArete("E", "F", 50, "RIVOLI_PALAIS_ROYAL");
        graphe.ajouterArete("B", "C", 100, "PALAIS_ROYAL_VENDOME");
        graphe.ajouterArete("F", "G", 50, "VENDOME_MADELEINE");
        graphe.ajouterArete("C", "G", 200, "MADELEINE_CONCORDE");
        graphe.ajouterArete("E", "K", 400, "MONTAIGNE_SAINT_GERMAIN");
        graphe.ajouterArete("K", "M", 200, "CHAMPS_ELYSEES_SAINT_GERMAIN");
        graphe.ajouterArete("L", "N", 200, "CONCORDE_OPERA");
        graphe.ajouterArete("J", "M", 400, "ROYALE_MONTAIGNE");
        graphe.ajouterArete("D", "J", 400, "GRENELLE_CHAMPS_ELYSEES");
        graphe.ajouterArete("K", "L", 300, "SAINT_HONORE_RIVOLI");
        graphe.ajouterArete("A", "B", 500, "SAINT_HONORE_ROYALE");
        graphe.ajouterArete("J", "K", 200, "ROYALE_GRENELLE");
        graphe.ajouterArete("F", "H", 50, "GRENELLE_BAC");
        graphe.ajouterArete("G", "H", 50, "BAC_VENDOME");
        graphe.ajouterArete("C", "E", 400, "PALAIS_ROYAL_GRENELLE");
        graphe.ajouterArete("H", "L", 300, "SAINT_GERMAIN_OPERA");
        graphe.ajouterArete("M", "N", 300, "SEVRES_SAINT_SULPICE");
        graphe.ajouterArete("P", "A", 0, "DEPOT_SAINT_HONORE");

        System.out.println("Graphe créé manuellement:");
        System.out.println("   " + graphe.getSommets().size() + " carrefours");
        System.out.println("   " + graphe.getAretes().size() + " rues");

        return graphe;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(" SYSTÈME DE COLLECTE DES DÉCHETS - PARIS ");
        System.out.println("==========================================");

        // REMPLACER le chargement par fichier par la création manuelle
        Graphe paris = creerGrapheManuellement();

        boolean continuer = true;

        while (continuer) {
            System.out.println("\nMENU PRINCIPAL");
            System.out.println("1. Problématique 1 - Collecte des encombrants");
            System.out.println("2. Problématique 2 - Collecte des poubelles aux habitations");
            System.out.println("3. Problématique 3 - Points de collecte et planification");
            System.out.println("4. Quitter");
            System.out.print("Votre choix (1-4) : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    menuProblematique1(paris, scanner);
                    break;
                case 2:
                    menuProblematique2(paris, scanner);
                    break;
                case 3:
                    menuProblematique3(paris, scanner);
                    break;
                case 4:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }

        System.out.println("\nMerci d'avoir utilisé le système de collecte parisien !");
        scanner.close();
    }

    // ===================================================================
    // PROBLÉMATIQUE 1 - COLLECTE DES ENCOMBRANTS
    // ===================================================================
    public static void menuProblematique1(Graphe graphe, Scanner scanner) {
        System.out.println("\nPROBLÉMATIQUE 1 - COLLECTE DES ENCOMBRANTS");

        boolean sousMenu = true;
        while (sousMenu) {
            System.out.println("\nSous-menu Problématique 1");
            System.out.println("1. Hypothèse 1.1 - Ramassage unique (1 encombrant)");
            System.out.println("2. Hypothèse 1.2 - Tournée groupée (10 encombrants)");
            System.out.println("3. Retour au menu principal");
            System.out.print("Votre choix (1-3) : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    hypothese1(graphe, scanner);
                    break;
                case 2:
                    hypothese2(graphe, scanner);
                    break;
                case 3:
                    sousMenu = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    // ===================================================================
    // PROBLÉMATIQUE 2 - COLLECTE DES POUBELLES AUX HABITATIONS
    // ===================================================================
    public static void menuProblematique2(Graphe graphe, Scanner scanner) {
        System.out.println("\nPROBLÉMATIQUE 2 - COLLECTE DES POUBELLES AUX HABITATIONS");
        System.out.println("Objectif: Trouver le circuit optimal pour collecter toutes les poubelles");
        System.out.println("Hypothèse H01: Toutes les rues à double sens");

        boolean sousMenu = true;
        while (sousMenu) {
            System.out.println("\nSOUS-MENU - PROBLÉMATIQUE 2");
            System.out.println("1. Analyser le graphe (recommandé en premier)");
            System.out.println("2. Cas idéal - Tous degrés pairs (Cycle eulérien)");
            System.out.println("3. Cas 2 sommets impairs (Chemin eulérien)");
            System.out.println("4. Cas général - Postier Chinois");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix (1-5) : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    analyserGrapheProblematique2(graphe);
                    break;
                case 2:
                    casIdeal(graphe, scanner);
                    break;
                case 3:
                    casDeuxSommetsImpairs(graphe, scanner);
                    break;
                case 4:
                    casGeneral(graphe, scanner);
                    break;

                case 5:
                    sousMenu = false;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    // ===================================================================
    // PROBLÉMATIQUE 3 - POINTS DE COLLECTE ET PLANIFICATION
    // ===================================================================
    public static void menuProblematique3(Graphe graphe, Scanner scanner) {
        System.out.println("\nPROBLÉMATIQUE 3 - POINTS DE COLLECTE ET PLANIFICATION");
        System.out.println("Fonctionnalité à implémenter");
        System.out.println("Cette problématique concerne :");
        System.out.println("- Optimisation des points de collecte (TSP)");
        System.out.println("- Planification des jours de collecte (coloration)");
        System.out.println("- Contraintes de capacité des camions");
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }


    // MÉTHODES DE LA PROBLÉMATIQUE 2

    private static void analyserGrapheProblematique2(Graphe graphe) {
        System.out.println("\nANALYSE POUR LA PROBLÉMATIQUE 2");

        GraphEuler analyseur = new GraphEuler(graphe);
        analyseur.analyserGraphe();

        System.out.println("\nRECOMMANDATION:");
        List<Sommet> impairs = analyseur.getSommetsImpairs();
        if (impairs.isEmpty()) {
            System.out.println("Utilisez l'option 2 (Cas idéal - Cycle eulérien)");
        } else if (impairs.size() == 2) {
            System.out.println("Utilisez l'option 3 (Cas 2 sommets impairs)");
            System.out.println("Point de départ recommandé: " + impairs.get(0));
        } else {
            System.out.println("Utilisez l'option 4 (Cas général - Postier Chinois)");
        }
    }

    private static void casIdeal(Graphe graphe, Scanner scanner) {
        System.out.println("\n- CAS IDÉAL - CYCLE EULÉRIEN -");
        System.out.println("Tous les sommets sont de degré pair - Parcours optimal sans répétition");

        GraphEuler analyseur = new GraphEuler(graphe);

        if (!analyseur.admetCycleEulerien()) {
            System.out.println("Ce graphe n'admet pas de cycle eulérien !");
            System.out.println("Utilisez l'option 1 pour analyser le graphe d'abord");
            return;
        }

        System.out.print("Point de départ (recommandé: Dépôt P) : ");
        String departStr = scanner.nextLine().toUpperCase().trim();
        Sommet depart = graphe.trouverSommet(departStr);

        if (depart == null) {
            System.out.println("Point non trouvé ! Utilisation du dépôt P par défaut");
            depart = graphe.trouverSommet("P");
            if (depart == null && !graphe.getSommets().isEmpty()) {
                depart = graphe.getSommets().get(0);
                System.out.println("Utilisation de " + depart + " comme point de départ");
            }
        }

        if (depart == null) {
            System.out.println("Impossible de trouver un point de départ !");
            return;
        }

        System.out.println("\nCalcul du cycle eulérien avec l'algorithme d'Hierholzer ");
        Hierholzer hierholzer = new Hierholzer(graphe);
        List<Sommet> circuit = hierholzer.trouverCycleEulerien(depart);

        if (circuit.isEmpty()) {
            System.out.println("Aucun circuit trouvé !");
            return;
        }

        afficherCircuitProblematique2(graphe, circuit, "CYCLE EULÉRIEN OPTIMAL");
    }

    private static void casDeuxSommetsImpairs(Graphe graphe, Scanner scanner) {
        System.out.println("\n- CAS 2 SOMMETS IMPAIRS - CHEMIN EULÉRIEN -");
        System.out.println("Parcours avec répétitions minimales");

        GraphEuler analyseur = new GraphEuler(graphe);

        if (!analyseur.admetCheminEulerien()) {
            System.out.println("Ce graphe n'admet pas de chemin eulérien");
            List<Sommet> impairs = analyseur.getSommetsImpairs();
            System.out.println("Nombre de sommets impairs : " + impairs.size());
            return;
        }

        List<Sommet> impairs = analyseur.getSommetsImpairs();
        System.out.println("Sommets impairs identifiés : " + impairs.get(0) + " et " + impairs.get(1));

        System.out.print("Point de départ (recommandé : " + impairs.get(0) + ") : ");
        String departStr = scanner.nextLine().toUpperCase().trim();
        Sommet depart = graphe.trouverSommet(departStr);

        if (depart == null) {
            System.out.println("Point non trouvé ! Utilisation de " + impairs.get(0) + " par défaut");
            depart = impairs.get(0);
        }

        System.out.println("\nCalcul du chemin eulérien...");
        CheminEulerien cheminEulerien = new CheminEulerien(graphe);
        List<Sommet> chemin = cheminEulerien.trouverCheminEulerien(depart);

        if (chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
            return;
        }

        afficherCircuitProblematique2(graphe, chemin, "CHEMIN EULÉRIEN AVEC RÉPÉTITIONS MINIMALES");
    }

    private static void casGeneral(Graphe graphe, Scanner scanner) {
        System.out.println("\n- CAS GÉNÉRAL - ALGORITHME DU POSTIER CHINOIS -");
        System.out.println("Circuit optimal visitant toutes les rues au moins une fois");

        GraphEuler analyseur = new GraphEuler(graphe);
        List<Sommet> impairs = analyseur.getSommetsImpairs();

        System.out.println(impairs.size() + " sommets de degré impair identifiés");
        System.out.println("Application de l'algorithme du Postier Chinois");

        System.out.print("Point de départ (recommandé: Dépôt P) : ");
        String departStr = scanner.nextLine().toUpperCase().trim();
        Sommet depart = graphe.trouverSommet(departStr);

        if (depart == null) {
            System.out.println("Point non trouvé ! Utilisation du dépôt par défaut");
            depart = graphe.trouverSommet("P");
            if (depart == null && !graphe.getSommets().isEmpty()) {
                depart = graphe.getSommets().get(0);
            }
        }

        if (depart == null) {
            System.out.println("Impossible de trouver un point de départ !");
            return;
        }

        System.out.println("Point de départ sélectionné : " + depart);
        System.out.println("\nLancement de l'algorithme du Postier Chinois");

        PostierChinois postier = new PostierChinois(graphe);
        List<Sommet> circuit = postier.trouverCircuit();

        if (circuit.isEmpty()) {
            System.out.println("Aucun circuit trouvé !");
            return;
        }

        afficherCircuitProblematique2(graphe, circuit, "CIRCUIT POSTIER CHINOIS");
    }

    private static void afficherCircuitProblematique2(Graphe graphe, List<Sommet> circuit, String titre) {
        System.out.println(titre);

        System.out.println("Circuit complet : " + circuit);

        int distance = 0;
        int etapes = 0;
        int repetitions = 0;
        Set<String> arêtesUniques = new HashSet<>();
        Set<String> arêtesParcourues = new HashSet<>();

        System.out.println("\nDÉTAIL DU PARCOURS DE COLLECTE:");

        for (int i = 0; i < circuit.size() - 1; i++) {
            Sommet current = circuit.get(i);
            Sommet next = circuit.get(i + 1);
            String nomRue = graphe.getNomRueEntre(current, next);
            int segment = graphe.getDistanceEntre(current, next);

            if (segment > 0) {
                distance += segment;
                etapes++;

                String cleArête = current + "-" + next;
                String cleArêteInverse = next + "-" + current;

                if (arêtesParcourues.contains(cleArête) || arêtesParcourues.contains(cleArêteInverse)) {
                    repetitions++;
                    System.out.printf("Étape %2d : %s -> %s via %-25s : %4dm (répétition)\n",
                            etapes, current, next, nomRue, segment);
                } else {
                    arêtesUniques.add(cleArête);
                    arêtesParcourues.add(cleArête);
                    arêtesParcourues.add(cleArêteInverse);
                    System.out.printf("Étape %2d : %s -> %s via %-25s : %4dm\n",
                            etapes, current, next, nomRue, segment);
                }
            }
        }

        System.out.println("-".repeat(70));

        int distanceMinimale = graphe.getDistanceTotale();
        double efficacite = (double) distanceMinimale / distance * 100;
        int arêtesTotal = graphe.getAretes().size();

        System.out.println("\nRAPPORT DE PERFORMANCE:");
        System.out.println("   Distance parcourue totale : " + distance + "m");
        System.out.println("   Distance minimale théorique : " + distanceMinimale + "m");
        System.out.println("   Surcharge due aux répétitions : " + (distance - distanceMinimale) + "m");
        System.out.println("   Efficacité du parcours : " + String.format("%.1f", efficacite) + "%");
        System.out.println("   Nombre total d'étapes : " + etapes);
        System.out.println("   Arêtes uniques parcourues : " + arêtesUniques.size() + "/" + arêtesTotal);
        System.out.println("   Nombre de répétitions : " + repetitions);

        System.out.println("\nANALYSE DE LA SOLUTION:");
        if (efficacite > 98) {
            System.out.println("   Solution optimale - Aucune répétition inutile");
        } else if (efficacite > 95) {
            System.out.println("   Solution quasi-optimale - Peu de répétitions");
        } else if (efficacite > 90) {
            System.out.println("   Solution acceptable - Quelques répétitions nécessaires");
        } else if (efficacite > 80) {
            System.out.println("   Solution sous-optimale - Répétitions modérées");
        } else {
            System.out.println("   Solution peu efficace - Nombreuses répétitions");
            System.out.println("   Vérifiez la connectivité du graphe");
        }

        System.out.println("\nCOLLECTE TERMINÉE:");
    }

    // ===================================================================
    // MÉTHODES DE LA PROBLÉMATIQUE 1
    // ===================================================================

    public static void hypothese1(Graphe graphe, Scanner scanner) {
        System.out.println("\nHYPOTHÈSE 1.1 - RAMASSAGE UNIQUE");

        System.out.println("RUES DISPONIBLES");
        System.out.println("A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P (P = Dépôt)");

        System.out.print("Point de départ : ");
        String departStr = scanner.nextLine().toUpperCase().trim();

        System.out.print("Point d'arrivée : ");
        String arriveeStr = scanner.nextLine().toUpperCase().trim();

        Sommet depart = graphe.trouverSommet(departStr);
        Sommet arrivee = graphe.trouverSommet(arriveeStr);

        if (depart == null || arrivee == null) {
            System.out.println("Point non trouvé !");
            return;
        }

        System.out.println("\nCALCUL DU PLUS COURT CHEMIN (DIJKSTRA)");
        List<Sommet> chemin = Dijkstra.calculerChemin(graphe, depart, arrivee);

        if (chemin.isEmpty()) {
            System.out.println("Aucun chemin trouvé !");
        } else {
            System.out.println("Chemin trouvé : " + chemin);

            int distance = 0;
            System.out.println("\nDÉTAIL DU TRAJET :");
            for (int i = 0; i < chemin.size() - 1; i++) {
                Sommet current = chemin.get(i);
                Sommet next = chemin.get(i + 1);
                String nomRue = graphe.getNomRueEntre(current, next);
                int segment = graphe.getDistanceEntre(current, next);
                distance += segment;
                System.out.println("  " + current + " -> " + next + " via " + nomRue + " : " + segment + "m");
            }

            System.out.println("\nDistance totale : " + distance + "m");
        }
    }

    public static void hypothese2(Graphe graphe, Scanner scanner) {
        System.out.println("\nHYPOTHÈSE 1.2 - TOURNÉE DE 10 ENCOMBRANTS");

        System.out.println("SOMMETS DISPONIBLES (lettres) :");
        System.out.println("A: SAINT_HONORE, B: SEVRES, C: MONTAIGNE, D: CHAMPS_ELYSEES");
        System.out.println("E: CONCORDE, F: RIVOLI, G: PALAIS_ROYAL, H: VENDOME");
        System.out.println("I: MADELEINE, J: SAINT_GERMAIN, K: OPERA, L: ROYALE");
        System.out.println("M: GRENELLE, N: BAC, O: SAINT_SULPICE, P: DEPOT");

        List<Sommet> points = new ArrayList<>();
        System.out.println("\nSaisir les 10 points de collecte (lettres A à O) :");
        System.out.println("Le dépôt P est le point de départ et d'arrivée fixe");

        for (int i = 1; i <= 10; i++) {
            System.out.print("Point " + i + " (lettre A-O) : ");
            String pointStr = scanner.nextLine().toUpperCase().trim();

            if (pointStr.equals("P")) {
                System.out.println("Le dépôt P est déjà inclus comme point de départ");
                i--;
                continue;
            }

            Sommet point = graphe.trouverSommet(pointStr);

            if (point == null) {
                System.out.println("Point non trouvé ! Utilisez les lettres A à O");
                i--;
            } else {
                points.add(point);
            }
        }

        Sommet depot = graphe.trouverSommet("P");

        System.out.println("\nCALCUL DE LA TOURNÉE OPTIMALE");
        List<Sommet> tournee = PlusProcheVoisin.calculerTournee(graphe, depot, points);

        if (tournee.isEmpty()) {
            System.out.println("Aucune tournée trouvée !");
            return;
        }

        System.out.println("Tournée optimale : " + tournee);

        int distance = 0;
        System.out.println("\nDÉTAIL DE LA TOURNÉE :");
        for (int i = 0; i < tournee.size() - 1; i++) {
            Sommet current = tournee.get(i);
            Sommet next = tournee.get(i + 1);
            String nomRue = graphe.getNomRueEntre(current, next);
            int segment = graphe.getDistanceEntre(current, next);
            distance += segment;
            System.out.println("  " + current + " -> " + next + " via " + nomRue + " : " + segment + "m");
        }

        System.out.println("\nDISTANCE TOTALE : " + distance + "m");
        System.out.println("Le camion part toujours du DEPOT (P) et y revient à la fin.");
    }
}