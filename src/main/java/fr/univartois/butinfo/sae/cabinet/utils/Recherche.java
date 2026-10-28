package fr.univartois.butinfo.sae.cabinet.utils;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Module de recherche transversale pour patients et médecins.
 * Regroupe les fonctionnalités de recherche avancée.
 */
public class Recherche {

    private final GestionPatients gestionPatients;
    private final GestionMedecins gestionMedecins;

    private static final Logger logger = LogManager.getLogger(Recherche.class);


    /**
     * Constructeur : injecte les services nécessaires.
     */
    public Recherche(GestionPatients gestionPatients, GestionMedecins gestionMedecins) {
        this.gestionPatients = gestionPatients;
        this.gestionMedecins = gestionMedecins;
    }

    // -------------------------
    // Recherche patients
    // -------------------------

    /**
     * Recherche des patients par nom ou prénom.
     */
    public List<Patient> rechercherPatientParNom(String terme) {
        List<Patient> resultats = gestionPatients.rechercherParNom(terme);
        afficherResultatsPatients(resultats, "Patients trouvés pour \"" + terme + "\"");
        return resultats;
    }

    /**
     * Recherche un patient par son numéro de sécurité sociale.
     */
    public Patient rechercherPatientParSecu(String numeroSecu) {
        Patient p = gestionPatients.rechercherParNumeroSecu(numeroSecu);
        if (p != null) {
            logger.info("Patient trouvé : " + p);
        } else {
            logger.info("Aucun patient trouvé pour le numéro de sécu : " + numeroSecu);
        }
        return p;
    }

    // -------------------------
    // Recherche médecins
    // -------------------------

    /**
     * Recherche des médecins par nom ou prénom.
     */
    public List<Medecin> rechercherMedecinParNom(String terme) {
        List<Medecin> resultats = gestionMedecins.rechercherParNom(terme);
        afficherResultatsMedecins(resultats, "Médecins trouvés pour \"" + terme + "\"");
        return resultats;
    }

    /**
     * Recherche des médecins par spécialité.
     */
    public List<Medecin> rechercherMedecinParSpecialite(Specialite specialite) {
        List<Medecin> resultats = gestionMedecins.rechercherParSpecialite(specialite);
        afficherResultatsMedecins(resultats,
                "Médecins de spécialité \"" + specialite.getLibelle() + "\"");
        return resultats;
    }

    /**
     * Recherche un médecin par son numéro d'ordre.
     */
    public Medecin rechercherMedecinParOrdre(String numeroOrdre) {
        Medecin m = gestionMedecins.rechercherParNumeroOrdre(numeroOrdre);
        if (m != null) {
            System.out.println("Médecin trouvé : " + m);
        } else {
            System.out.println("Aucun médecin trouvé pour le n° d'ordre : " + numeroOrdre);
        }
        return m;
    }

    // -------------------------
    // Affichage des résultats
    // -------------------------

    private void afficherResultatsPatients(List<Patient> liste, String titre) {
        logger.info("--- " + titre + " (" + liste.size() + " résultat(s)) ---");
        if (liste.isEmpty()) {
            logger.info("  Aucun résultat.");
        } else {
            for (Patient p : liste) logger.info("  " + p);
        }
    }

    private void afficherResultatsMedecins(List<Medecin> liste, String titre) {
        logger.info("--- " + titre + " (" + liste.size() + " résultat(s)) ---");
        if (liste.isEmpty()) {
            logger.info("  Aucun résultat.");
        } else {
            for (Medecin m : liste) logger.info("  " + m);
        }
    }

    @Override
    public String toString() {
        return "Module Recherche { patients=" + gestionPatients.getNombrePatients() +
                ", médecins=" + gestionMedecins.getNombreMedecins() + " }";
    }
}
