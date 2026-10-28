package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Énumération des spécialités médicales disponibles dans le cabinet.
 */
public enum Specialite {
    GENERALISTE("Médecin généraliste"),
    CARDIOLOGUE("Cardiologie"),
    DERMATOLOGUE("Dermatologie"),
    GYNECOLOGUE("Gynécologie"),
    NEUROLOGUE("Neurologie"),
    OPHTALMOLOGUE("Ophtalmologie"),
    PEDIATRE("Pédiatrie"),
    PSYCHIATRE("Psychiatrie"),
    RADIOLOGUE("Radiologie"),
    URGENTISTE("Urgences");

    // Libellé lisible de la spécialité
    private final String libelle;

    /**
     * Constructeur de l'énumération.
     * @param libelle le nom complet de la spécialité
     */
    Specialite(String libelle) {
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
