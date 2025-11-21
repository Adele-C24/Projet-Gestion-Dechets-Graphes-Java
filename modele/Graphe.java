package modele.modele;

import java.util.*;
import java.io.*;

public class Graphe {
    private List<Sommet> sommets;
    private List<Arete> aretes;
    private Map<String, Sommet> sommetsMap;

    public Graphe() {
        this.sommets = new ArrayList<>();
        this.aretes = new ArrayList<>();
        this.sommetsMap = new HashMap<>();
    }

    // Méthode pour charger depuis un fichier
    public void chargerDepuisFichier(String nomFichier) {
        try {
            File file = new File(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine().trim();

                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue;
                }

                String[] parties = ligne.split("-");
                if (parties.length == 3) {
                    String nom1 = parties[0];
                    String nom2 = parties[1];
                    int distance = Integer.parseInt(parties[2]);

                    Sommet s1 = getOuCreerSommet(nom1);
                    Sommet s2 = getOuCreerSommet(nom2);

                    Arete arete = new Arete(s1, s2, distance);
                    aretes.add(arete);
                }
            }
            scanner.close();
            System.out.println("Carte chargée : " + sommets.size() + " carrefours, " + aretes.size() + " rues");

        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé : " + nomFichier);
        } catch (Exception e) {
            System.out.println("Erreur lecture fichier : " + e.getMessage());
        }
    }

    private Sommet getOuCreerSommet(String nom) {
        if (!sommetsMap.containsKey(nom)) {
            Sommet nouveau = new Sommet(nom);
            sommetsMap.put(nom, nouveau);
            sommets.add(nouveau);
        }
        return sommetsMap.get(nom);
    }

    // GETTERS
    public Sommet trouverSommet(String nom) {
        return sommetsMap.get(nom);
    }

    public List<Sommet> getSommets() {
        return new ArrayList<>(sommets);
    }

    public List<Arete> getAretes() {
        return new ArrayList<>(aretes);
    }

    public int getDistanceEntre(Sommet s1, Sommet s2) {
        for (Arete arete : aretes) {
            if ((arete.getDebut().equals(s1) && arete.getFin().equals(s2)) ||
                    (arete.getDebut().equals(s2) && arete.getFin().equals(s1))) {
                return arete.getDistance();
            }
        }
        return -1; // Pas connectés
    }

    // Afficher le graphe (pour debug)
    public void afficher() {
        System.out.println("=== GRAPHE ===");
        for (Sommet s : sommets) {
            System.out.print(s.getNom() + " -> ");
            for (Arete a : aretes) {
                if (a.getDebut().equals(s)) {
                    System.out.print(a.getFin() + "(" + a.getDistance() + ") ");
                } else if (a.getFin().equals(s)) {
                    System.out.print(a.getDebut() + "(" + a.getDistance() + ") ");
                }
            }
            System.out.println();
        }
    }
}