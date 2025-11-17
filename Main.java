package application;

import modele.Graphe;
import modele.Sommet;
import algo.Dijkstra;
import algo.PlusProcheVoisin;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

          System.out.println(" SYSTÈME DE COLLECTE DES DÉCHETS - PARIS ");

        // Créer et charger le graphe
        Graphe paris = new Graphe();
        paris.chargerDepuisFichier("Maps/map1.txt");

        boolean continuer = true;

        while (continuer) {
            System.out.println("\n MENU PRINCIPAL");
            System.out.println("1. Problématique 1 - Collecte des encombrants");
            System.out.println("2. Problématique 2 - Collecte des poubelles aux habitations");
            System.out.println("3. Problématique 3 - Points de collecte et planification");
            System.out.println("4. Quitter");
            System.out.print("Votre choix (1-4) : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Vider le buffer

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
                    System.out.println(" Choix invalide !");
            }
        }

        System.out.println("\n Merci d'avoir utilisé le système de collecte parisien !");
        scanner.close();
    }

    // SOUS-MENU PROBLÉMATIQUE 1
    public static void menuProblematique1(Graphe graphe, Scanner scanner) {
        System.out.println("\nPROBLÉMATIQUE 1 - COLLECTE DES ENCOMBRANTS ");

        boolean sousMenu = true;
        while (sousMenu) {
            System.out.println("\n Sous-menu Problématique 1");
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
                    System.out.println(" Choix invalide !");
            }
        }
    }

    // PROBLÉMATIQUE 2 - À IMPLÉMENTER
    public static void menuProblematique2(Graphe graphe, Scanner scanner) {
        System.out.println("\n=== PROBLÉMATIQUE 2 - COLLECTE DES POUBELLES AUX HABITATIONS ===");
        System.out.println("Fonctionnalité à implémenter ");
        System.out.println("Cette problématique concerne :");
        System.out.println("- Cas idéal : tous degrés pairs (cycle eulérien)");
        System.out.println("- Cas 2 sommets impairs (chemin eulérien)");
        System.out.println("- Cas général : algorithme du Postier Chinois");
        System.out.println("\n Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    // PROBLÉMATIQUE 3 - À IMPLÉMENTER
    public static void menuProblematique3(Graphe graphe, Scanner scanner) {
        System.out.println("\n=== PROBLÉMATIQUE 3 - POINTS DE COLLECTE ET PLANIFICATION ===");
        System.out.println("🚧 Fonctionnalité à implémenter 🚧");
        System.out.println("Cette problématique concerne :");
        System.out.println("- Optimisation des points de collecte (TSP)");
        System.out.println("- Planification des jours de collecte (coloration)");
        System.out.println("- Contraintes de capacité des camions");
        System.out.println("\n Appuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    // HYPOTHÈSE 1.1 - RAMASSAGE UNIQUE
    public static void hypothese1(Graphe graphe, Scanner scanner) {
        System.out.println("\nHYPOTHÈSE 1.1 - RAMASSAGE UNIQUE");

        // Afficher les sommets disponibles
        System.out.println("RUES DISPONIBLES");
        System.out.println("SAINT_HONORE, RIVOLI, PALAIS_ROYAL, ROYALE, GRENELLE");
        System.out.println("BAC, VENDOME, MADELEINE, SEVRES, MONTAIGNE");
        System.out.println("CHAMPS_ELYSEES, CONCORDE, SAINT_GERMAIN, OPERA, SAINT_SULPICE, DEPOT");

        System.out.print("Point de départ : ");
        String departStr = scanner.nextLine().toUpperCase().trim();

        System.out.print("Point d'arrivée : ");
        String arriveeStr = scanner.nextLine().toUpperCase().trim();

        Sommet depart = graphe.trouverSommet(departStr);
        Sommet arrivee = graphe.trouverSommet(arriveeStr);

        if (depart == null || arrivee == null) {
            System.out.println(" Point non trouvé !");
            return;
        }

        System.out.println("\n CALCUL DIJKSTRA");
        List<Sommet> chemin = Dijkstra.calculerChemin(graphe, depart, arrivee);

        if (chemin.isEmpty()) {
            System.out.println(" Aucun chemin trouvé !");
        } else {
            System.out.println("Chemin trouvé : " + chemin);

            int distance = 0;
            System.out.println("\nDÉTAIL DU TRAJET :");
            for (int i = 0; i < chemin.size() - 1; i++) {
                int segment = graphe.getDistanceEntre(chemin.get(i), chemin.get(i+1));
                distance += segment;
                System.out.println("  " + chemin.get(i) + " → " + chemin.get(i+1) + " : " + segment + "m");
            }
            System.out.println("DISTANCE TOTALE : " + distance + "m");
        }
    }

    // HYPOTHÈSE 1.2 - TOURNÉE GROUPÉE
    public static void hypothese2(Graphe graphe, Scanner scanner) {
        System.out.println("\n HYPOTHÈSE 1.2 - TOURNÉE DE 10 ENCOMBRANTS");

        // Afficher les sommets disponibles
        System.out.println(" RUES DISPONIBLES ");
        System.out.println("SAINT_HONORE, RIVOLI, PALAIS_ROYAL, ROYALE, GRENELLE");
        System.out.println("BAC, VENDOME, MADELEINE, SEVRES, MONTAIGNE");
        System.out.println("CHAMPS_ELYSEES, CONCORDE, SAINT_GERMAIN, OPERA, SAINT_SULPICE, DEPOT");

        List<Sommet> points = new ArrayList<>();
        System.out.println("\nSaisir les 10 points de collecte :");

        for (int i = 1; i <= 10; i++) {
            System.out.print("Point " + i + " : ");
            String pointStr = scanner.nextLine().toUpperCase().trim();
            Sommet point = graphe.trouverSommet(pointStr);

            if (point == null) {
                System.out.println(" Point non trouvé !");
                i--; // Recommencer ce point
            } else {
                points.add(point);
            }
        }

        // DÉPART FIXE : DEPOT
        Sommet depot = graphe.trouverSommet("DEPOT");

        System.out.println("\n CALCUL TOURNÉE OPTIMALE");

        // CORRECTION : Appel correct selon votre signature de méthode
        List<Sommet> tournee = PlusProcheVoisin.calculerTournee(graphe, depot, points);

        if (tournee.isEmpty()) {
            System.out.println(" Aucune tournée trouvée !");
            return;
        }

        System.out.println("Tournée optimale : " + tournee);

        int distance = 0;
        System.out.println("\n DÉTAIL DE LA TOURNÉE :");
        for (int i = 0; i < tournee.size() - 1; i++) {
            int segment = graphe.getDistanceEntre(tournee.get(i), tournee.get(i+1));
            distance += segment;
            System.out.println("  " + tournee.get(i) + " → " + tournee.get(i+1) + " : " + segment + "m");
        }
        System.out.println(" DISTANCE TOTALE : " + distance + "m");

        System.out.println("\n Le camion part toujours du DEPOT et y revient à la fin.");
    }
}