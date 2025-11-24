package modele;

import java.util.*;

public class GraphEuler {
    private Graphe graphe;

    public GraphEuler(Graphe graphe) {
        this.graphe = graphe;
    }

    // Vérifie si le graphe est connexe
    public boolean estConnexe() {
        if (graphe.getSommets().isEmpty()) {
            System.out.println("  Graphe vide -> considéré comme connexe");
            return true;
        }

        Set<Sommet> visites = new HashSet<>();
        Sommet premier = graphe.getSommets().get(0);
        parcoursEnProfondeur(premier, visites);

        boolean connexe = (visites.size() == graphe.getSommets().size());
        System.out.println("  Connexité: " + connexe + " (" + visites.size() + "/" + graphe.getSommets().size() + " sommets visités)");
        return connexe;
    }

    private void parcoursEnProfondeur(Sommet courant, Set<Sommet> visites) {
        visites.add(courant);

        for (Sommet voisin : graphe.getVoisins(courant)) {
            if (!visites.contains(voisin)) {
                parcoursEnProfondeur(voisin, visites);
            }
        }
    }

    // Calcule le degré d'un sommet
    public int getDegre(Sommet sommet) {
        int degre = 0;
        for (Arete arete : graphe.getAretes()) {
            if (arete.getDebut().equals(sommet) || arete.getFin().equals(sommet)) {
                degre++;
            }
        }
        return degre;
    }

    // Trouve tous les sommets de degré impair
    public List<Sommet> getSommetsImpairs() {
        List<Sommet> impairs = new ArrayList<>();
        for (Sommet sommet : graphe.getSommets()) {
            int degre = getDegre(sommet);
            if (degre % 2 != 0) {
                impairs.add(sommet);
            }
        }
        return impairs;
    }

    // Vérifie si le graphe admet un cycle eulérien
    public boolean admetCycleEulerien() {
        boolean connexe = estConnexe();
        List<Sommet> impairs = getSommetsImpairs();
        boolean cycle = connexe && impairs.isEmpty();

        System.out.println("  Cycle eulérien: " + cycle);
        System.out.println("  - Connexe: " + connexe);
        System.out.println("  - Sommets impairs: " + impairs.size());

        return cycle;
    }

    // Vérifie si le graphe admet un chemin eulérien
    public boolean admetCheminEulerien() {
        boolean connexe = estConnexe();
        List<Sommet> impairs = getSommetsImpairs();
        boolean chemin = connexe && impairs.size() == 2;

        System.out.println("  Chemin eulérien: " + chemin);
        System.out.println("  - Connexe: " + connexe);
        System.out.println("  - Sommets impairs: " + impairs.size());

        return chemin;
    }

    // Analyse complète du graphe
    public void analyserGraphe() {
        System.out.println("\nANALYSE EULÉRIENNE DÉTAILLÉE");

        // Informations de base
        System.out.println("• " + graphe.getSommets().size() + " sommets, " + graphe.getAretes().size() + " arêtes");

        // Connexité
        boolean connexe = estConnexe();

        // Analyse des degrés
        List<Sommet> impairs = getSommetsImpairs();
        System.out.println("• " + impairs.size() + " sommets de degré impair");

        // Détail des degrés
        System.out.println("\n DEGRÉS PAR SOMMET:");
        for (Sommet sommet : graphe.getSommets()) {
            int degre = getDegre(sommet);
            String type = (degre % 2 == 0) ? "PAIR" : "IMPAIR";
            System.out.printf("  %s: degré %d (%s)\n", sommet.getNom(), degre, type);
        }

        // Conclusion
        System.out.println("\nCONCLUSION:");
        if (!connexe) {
            System.out.println(" IMPOSSIBLE - Graphe non connexe");
        } else if (impairs.isEmpty()) {
            System.out.println("CYCLE EULÉRIEN - Parcours optimal possible");
            System.out.println(" Utiliser l'algorithme d'Hierholzer");
        } else if (impairs.size() == 2) {
            System.out.println("CHEMIN EULÉRIEN - Parcours avec répétitions minimales");
            System.out.println(" Points de départ/arrivée: " + impairs.get(0) + " et " + impairs.get(1));
        } else {
            System.out.println("POSTIER CHINOIS - " + impairs.size() + " sommets impairs");
            System.out.println("Nécessite la duplication d'arêtes");
        }
    }
}