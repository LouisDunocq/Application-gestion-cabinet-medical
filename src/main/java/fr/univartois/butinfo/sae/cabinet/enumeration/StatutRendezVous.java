package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Énumération des statuts possibles d'un rendez-vous.
 */
public enum StatutRendezVous {
    PLANIFIE("Planifié"),
    CONFIRME("Confirmé"),
    ANNULE("Annulé"),
    TERMINE("Terminé");

    private final String libelle;

    StatutRendezVous(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
