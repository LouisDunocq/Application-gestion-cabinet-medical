package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class PatientController {

    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private VBox panelDetails;
    @FXML private VBox panelFormulaire;
    @FXML private Label lblNomComplet;
    @FXML private Label lblTelephone;
    @FXML private Label lblSecu;
    @FXML private Label lblTaux;
    @FXML private Label lblErreur;
    @FXML private Button btnModifier;
    @FXML private Label lblTitreForm;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtSecu;
    @FXML private TextField txtTaux;

    private GestionPatients gestionPatients;
    private Patient patientEnEdition;

    public void setGestionPatients(GestionPatients gestionPatients) {
        this.gestionPatients = gestionPatients;
        rafraichirTable();
    }

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            basculerVersDetails();
            afficherDetailsPatient(newValue);
        });
    }

    private void rafraichirTable() {
        if (this.gestionPatients != null) {
            patientTable.setItems(FXCollections.observableArrayList(this.gestionPatients.listerTousLesPatients()));
        }
    }

    private void afficherDetailsPatient(Patient patient) {
        if (patient != null) {
            lblNomComplet.setText(patient.getNomComplet());
            lblTelephone.setText(patient.getTelephone());
            lblSecu.setText(patient.getNumeroSecu());
            lblTaux.setText((patient.getTauxRemboursement() * 100) + " %");
            btnModifier.setDisable(false);
        } else {
            lblNomComplet.setText("");
            lblTelephone.setText("");
            lblSecu.setText("");
            lblTaux.setText("");
            btnModifier.setDisable(true);
        }
    }

    @FXML
    private void handleAjouter() {
        this.patientEnEdition = null;
        lblTitreForm.setText("Ajouter un nouveau Patient");
        txtNom.clear();
        txtPrenom.clear();
        txtTelephone.clear();
        txtSecu.clear();
        txtSecu.setDisable(false);
        txtTaux.setText("0.75");
        if (lblErreur != null) lblErreur.setText("");
        basculerVersFormulaire();
    }

    @FXML
    private void handleModifier() {
        Patient selection = patientTable.getSelectionModel().getSelectedItem();
        if (selection != null) {
            this.patientEnEdition = selection;
            lblTitreForm.setText("Modifier le Patient");
            txtNom.setText(selection.getNom());
            txtPrenom.setText(selection.getPrenom());
            txtTelephone.setText(selection.getTelephone());
            txtSecu.setText(selection.getNumeroSecu());
            txtSecu.setDisable(true);
            txtTaux.setText(String.valueOf(selection.getTauxRemboursement()));
            if (lblErreur != null) lblErreur.setText(""); 
            basculerVersFormulaire();
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtSecu.getText().isEmpty()) {
            if (lblErreur != null) {
                lblErreur.setText("Erreur : Veuillez remplir les champs obligatoires (Nom, Prénom, Sécu).");
            }
            return;
        }

        try {
            double taux = Double.parseDouble(txtTaux.getText().replace(",", "."));
            if (patientEnEdition == null) {
                Patient nouveau = new Patient(
                        txtNom.getText(), 
                        txtPrenom.getText(), 
                        txtTelephone.getText(),
                        "email@test.fr", 
                        "Adresse", 
                        LocalDate.now().minusYears(20),
                        txtSecu.getText(), 
                        taux
                );
                gestionPatients.ajouterPatient(nouveau);
            } else {
                gestionPatients.mettreAJourPatient(
                        patientEnEdition.getId(), 
                        txtNom.getText(), 
                        txtPrenom.getText(),
                        txtTelephone.getText(), 
                        patientEnEdition.getEmail(),
                        patientEnEdition.getAdresse(), 
                        taux
                );
            }

            if (lblErreur != null) lblErreur.setText("");
            rafraichirTable();
            basculerVersDetails();
            patientTable.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            if (lblErreur != null) {
                lblErreur.setText("Erreur : Le format du taux de remboursement est invalide (ex: 0.75).");
            }
        }
    }

    @FXML
    private void handleAnnuler() {
        basculerVersDetails();
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