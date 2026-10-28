package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MedicamentsController {

    @FXML private TableView<Medicament> medicamentTable;
    @FXML private TableColumn<Medicament, String> colNom;
    @FXML private TableColumn<Medicament, String> colPrix;
    @FXML private VBox panelDetails;
    @FXML private VBox panelFormulaire;
    @FXML private Label lblNom;
    @FXML private Label lblPrix;
    @FXML private Label lblDescription;
    @FXML private Label lblTitreForm;
    @FXML private Label lblErreur;
    @FXML private Button btnModifier;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;
    @FXML private TextArea txtDescription;

    private GestionMedicaments gestionMedicaments;
    private Medicament medicamentEnEdition;

    public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
        this.gestionMedicaments = gestionMedicaments;
        rafraichirTable();
    }

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrix.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrix()) + " €"));

        medicamentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            basculerVersDetails();
            afficherDetailsMedicament(newValue);
        });
    }

    private void rafraichirTable() {
        if (this.gestionMedicaments != null) {
            medicamentTable.setItems(FXCollections.observableArrayList(this.gestionMedicaments.listerTousLesMedicaments()));
        }
    }

    private void afficherDetailsMedicament(Medicament medicament) {
        if (medicament != null) {
            lblNom.setText(medicament.getNom());
            lblPrix.setText(medicament.getPrix() + " €");
            lblDescription.setText(medicament.getDescription());
            btnModifier.setDisable(false);
        } else {
            lblNom.setText("");
            lblPrix.setText("");
            lblDescription.setText("");
            btnModifier.setDisable(true);
        }
    }

    @FXML
    public void handleAjouter() {
        this.medicamentEnEdition = null;
        lblTitreForm.setText("Ajouter un nouveau Médicament");
        txtNom.clear();
        txtPrix.clear();
        txtDescription.clear();
        lblErreur.setText("");
        basculerVersFormulaire();
    }

    @FXML
    public void handleModifier() {
        Medicament medocSelection = medicamentTable.getSelectionModel().getSelectedItem();
        if (medocSelection != null) {
            this.medicamentEnEdition = medocSelection;
            lblTitreForm.setText("Modifier le Médicament");
            txtNom.setText(medocSelection.getNom());
            txtPrix.setText(String.valueOf(medocSelection.getPrix()));
            txtDescription.setText(medocSelection.getDescription());
            lblErreur.setText("");
            basculerVersFormulaire();
        }
    }

    @FXML
    public void handleAnnuler() {
        basculerVersDetails();
    }

    @FXML
    public void handleEnregistrer() {
        if (txtNom.getText().isEmpty() || txtDescription.getText().isEmpty() || txtPrix.getText().isEmpty()) {
            lblErreur.setText("Erreur : Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            double prix = Double.parseDouble(txtPrix.getText().replace(",", "."));

            if (medicamentEnEdition == null) {
                Medicament nouveau = new Medicament(txtNom.getText(), txtDescription.getText(), prix);
                gestionMedicaments.ajouterMedicament(nouveau);
            } else {
                medicamentEnEdition.setNom(txtNom.getText());
                medicamentEnEdition.setDescription(txtDescription.getText());
                medicamentEnEdition.setPrix(prix);
            }

            lblErreur.setText("");
            rafraichirTable();
            basculerVersDetails();
            medicamentTable.getSelectionModel().clearSelection();

        }
        catch (NumberFormatException e) {
            lblErreur.setText("Erreur : Le format du prix est invalide (ex: 12.50).");
        }
    }

    private void basculerVersFormulaire() {
        panelDetails.setVisible(false);
        panelFormulaire.setVisible(true);
    }

    private void basculerVersDetails() {
        panelDetails.setVisible(true);
        panelFormulaire.setVisible(false);
    }
}