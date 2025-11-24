package modele;

import java.util.*;
import java.io.*;

// Représente le graphe complet de la ville
// Contient tous les sommets et arêtes

public class Graphe {
    private List<Sommet> sommets;
    private List<Arete> aretes;
    private Map<String, Sommet> sommetsMap;

    public Graphe() {
        this.sommets = new ArrayList<>();
        this.aretes = new ArrayList<>();
        this.sommetsMap = new HashMap<>();
    }

    // Charge le graphe depuis un fichier texte
    // Format: A-B-NOM_RUE-DISTANCE

    public void chargerDepuisFichier(String nomFichier) {
        try {
            File file = new File(nomFichier);
            Scanner scanner = new Scanner(file);
            int compteurAretes = 0;

            System.out.println(" Chargement du fichier: " + nomFichier);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine().trim();

                // Ignorer les lignes vides et les commentaires
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                // Format: A-B-NOM_RUE-DISTANCE
                String[] parties = ligne.split("-");
                if (parties.length == 4) {
                    String nom1 = parties[0].trim();
                    String nom2 = parties[1].trim();
                    String nomRue = parties[2].trim();
                    int distance = Integer.parseInt(parties[3].trim());

                    // Créer ou récupérer les sommets
                    Sommet s1 = getOuCreerSommet(nom1);
                    Sommet s2 = getOuCreerSommet(nom2);

                    // Créer l'arête (dans les deux sens pour un graphe non orienté)
                    Arete arete = new Arete(s1, s2, distance, nomRue);
                    aretes.add(arete);
                    compteurAretes++;

                    System.out.println("✓ Rue ajoutée: " + arete);
                } else {
                    System.out.println(" Ligne ignorée (format incorrect): " + ligne);
                }
            }
            scanner.close();

            System.out.println("\n Chargement terminé:");
            System.out.println("   • " + sommets.size() + " carrefours chargés");
            System.out.println("   • " + compteurAretes + " rues chargées");
            System.out.println("   • Sommets: " + sommetsMap.keySet());

        } catch (FileNotFoundException e) {
            System.out.println(" Fichier non trouvé: " + nomFichier);
        } catch (Exception e) {
            System.out.println(" Erreur lors de la lecture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Récupère un sommet existant ou le crée s'il n'existe pas

    private Sommet getOuCreerSommet(String nom) {
        if (!sommetsMap.containsKey(nom)) {
            Sommet nouveau = new Sommet(nom);
            sommetsMap.put(nom, nouveau);
            sommets.add(nouveau);
            System.out.println("  + Sommet créé: " + nom);
        }
        return sommetsMap.get(nom);
    }

    // Trouve le nom de la rue entre deux sommets
    public String getNomRueEntre(Sommet s1, Sommet s2) {
        if (s1 == null || s2 == null) return "Rue inconnue";

        for (Arete arete : aretes) {
            if ((arete.getDebut().equals(s1) && arete.getFin().equals(s2)) ||
                    (arete.getDebut().equals(s2) && arete.getFin().equals(s1))) {
                return arete.getNomRue();
            }
        }
        return "Rue inconnue";
    }

    // Calcule la distance entre deux sommets

    public int getDistanceEntre(Sommet s1, Sommet s2) {
        if (s1 == null || s2 == null) return -1;

        for (Arete arete : aretes) {
            if ((arete.getDebut().equals(s1) && arete.getFin().equals(s2)) ||
                    (arete.getDebut().equals(s2) && arete.getFin().equals(s1))) {
                return arete.getDistance();
            }
        }
        return -1; // Pas connectés
    }

    // Trouve un sommet par son nom
    public Sommet trouverSommet(String nom) {
        return sommetsMap.get(nom);
    }

    // Retourne la liste des voisins d'un sommet
    public List<Sommet> getVoisins(Sommet sommet) {
        List<Sommet> voisins = new ArrayList<>();
        for (Arete arete : aretes) {
            if (arete.getDebut().equals(sommet)) {
                voisins.add(arete.getFin());
            } else if (arete.getFin().equals(sommet)) {
                voisins.add(arete.getDebut());
            }
        }
        return voisins;
    }

    // Ajoute une arête au graphe (méthode utilitaire)
    public void ajouterArete(String nomDebut, String nomFin, int distance, String nomRue) {
        Sommet s1 = getOuCreerSommet(nomDebut);
        Sommet s2 = getOuCreerSommet(nomFin);
        Arete arete = new Arete(s1, s2, distance, nomRue);
        aretes.add(arete);
    }

    // Calcule la distance totale minimale (somme de toutes les arêtes uniques)
    public int getDistanceTotale() {
        int total = 0;
        Set<String> arêtesComptées = new HashSet<>();

        for (Arete arete : aretes) {
            String cle = arete.getDebut() + "-" + arete.getFin();
            String cleInverse = arete.getFin() + "-" + arete.getDebut();

            if (!arêtesComptées.contains(cle) && !arêtesComptées.contains(cleInverse)) {
                total += arete.getDistance();
                arêtesComptées.add(cle);
            }
        }
        return total;
    }

    // Affiche l'état complet du graphe pour debug
    public void afficherEtatComplet() {
        System.out.println("\n=== ÉTAT COMPLET DU GRAPHE ===");
        System.out.println("Sommets (" + sommets.size() + "): " + sommetsMap.keySet());
        System.out.println("Arêtes (" + aretes.size() + "):");

        for (Arete arete : aretes) {
            System.out.println("  " + arete);
        }

        System.out.println("\nConnexions par sommet:");
        for (Sommet sommet : sommets) {
            List<Sommet> voisins = getVoisins(sommet);
            System.out.print("  " + sommet + " -> ");
            for (Sommet voisin : voisins) {
                int dist = getDistanceEntre(sommet, voisin);
                System.out.print(voisin + "(" + dist + "m) ");
            }
            System.out.println();
        }
    }

    // GETTERS
    public List<Sommet> getSommets() {
        return new ArrayList<>(sommets);
    }

    public List<Arete> getAretes() {
        return new ArrayList<>(aretes);
    }
}