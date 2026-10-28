package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.LignePrescription;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Service de gestion des ordonnances médicales.
 */
public class GestionOrdonnances {

    private final ObservableList<Ordonnance> ordonnances;

    private static final Logger logger = LogManager.getLogger(GestionOrdonnances.class);

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionOrdonnances() {
        this.ordonnances = FXCollections.observableArrayList();
    }

    // -------------------------
    // Gestion des ordonnances
    // -------------------------

    /**
     * Crée une nouvelle ordonnance vide (sans prescriptions) et l'enregistre.
     * @return l'ordonnance créée
     */
    public Ordonnance creerOrdonnance(Medecin medecin, Patient patient,
                                       LocalDate dateValidite, String remarques) {
        Ordonnance ordo = new Ordonnance(medecin, patient, dateValidite, remarques);
        ordonnances.add(ordo);
        logger.info("Ordonnance créée (ID=" + ordo.getId() + ") pour " +
                           patient.getNomComplet() + " par Dr " + medecin.getNomComplet());
        return ordo;
    }

    /**
     * Ajoute une ligne de prescription à une ordonnance existante.
     */
    public void ajouterPrescription(int ordonnanceId, Medicament medicament, int quantite,
                                        String posologie, int dureeJours, String instructions) {
        Ordonnance ordo = rechercherParId(ordonnanceId);
        if (ordo == null) {
            logger.error("ERREUR : Ordonnance ID=" + ordonnanceId + " introuvable.");
            return;
        }
        LignePrescription ligne = new LignePrescription(medicament, quantite, posologie, dureeJours, instructions);
        ordo.ajouterPrescription(ligne);
        logger.info("Prescription ajoutée : " + medicament.getNom() +
                           " → ordonnance ID=" + ordonnanceId);
    }

    /**
     * Recherche une ordonnance par son ID.
     */
    public Ordonnance rechercherParId(int id) {
        for (Ordonnance o : ordonnances) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    /**
     * Retourne toutes les ordonnances d'un patient.
     */
    public List<Ordonnance> getOrdonnancesPatient(Patient patient) {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.getPatient().equals(patient)) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne toutes les ordonnances émises par un médecin.
     */
    public List<Ordonnance> getOrdonnancesMedecin(Medecin medecin) {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.getMedecin().equals(medecin)) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne uniquement les ordonnances encore valides à ce jour.
     */
    public List<Ordonnance> getOrdonnancesValides() {
        List<Ordonnance> resultats = new ArrayList<>();
        for (Ordonnance o : ordonnances) {
            if (o.estValide()) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne la liste complète des ordonnances.
     */
    public List<Ordonnance> listerToutesLesOrdonnances() {
        return new ArrayList<>(ordonnances);
    }

    /**
     * Retourne le nombre total d'ordonnances.
     */
    public int getNombreOrdonnances() {
        return ordonnances.size();
    }

}
