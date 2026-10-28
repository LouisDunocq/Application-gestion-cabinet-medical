package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MedecinController {

    @FXML private TableView<Medecin> medecinTable;
    @FXML private TableColumn<Medecin, String> colNom;
    @FXML private TableColumn<Medecin, String> colPrenom;
    @FXML private VBox panelDetails;
    @FXML private VBox panelFormulaire;
    @FXML private Label lblNomComplet;
    @FXML private Label lblTarifHoraire;
    @FXML private Label lblTelephone;
    @FXML private Label lblEmail;
    @FXML private Label lblSpecialite;
    @FXML private Label lblErreur;
    @FXML private Button btnModifier;
    @FXML private Label lblTitreForm;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtHoraire;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSpecialite;

    private GestionMedecins gestionMedecins;
    private Medecin medecinEnEdition;

    public void setGestionMedecins(GestionMedecins gestionMedecins) {
        this.gestionMedecins = gestionMedecins;
        rafraichirTable();
    }

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        medecinTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            basculerVersDetails();
            afficherDetailsMedecin(newValue);
        });
    }

    private void rafraichirTable() {
        if (this.gestionMedecins != null) {
            medecinTable.setItems(FXCollections.observableArrayList(this.gestionMedecins.listerTousLesMedecins()));
        }
    }

    private void afficherDetailsMedecin(Medecin medecin) {
        if (medecin != null) {
            lblNomComplet.setText(medecin.getNom() + " " + medecin.getPrenom());
            lblTarifHoraire.setText(medecin.getTarifHoraire() + " €/h");
            lblTelephone.setText(medecin.getTelephone());
            lblEmail.setText(medecin.getEmail());
            lblSpecialite.setText(medecin.getSpecialite() != null ? medecin.getSpecialite().name() : "Non définie");
            btnModifier.setDisable(false);
        } else {
            lblNomComplet.setText("");
            lblTarifHoraire.setText("");
            lblTelephone.setText("");
            lblEmail.setText("");
            lblSpecialite.setText("");
            btnModifier.setDisable(true);
        }
    }

    @FXML
    private void handleAjouter() {
        this.medecinEnEdition = null;
        lblTitreForm.setText("Ajouter un nouveau Médecin");
        txtNom.clear();
        txtPrenom.clear();
        txtHoraire.clear();
        txtTelephone.clear();
        txtEmail.clear();
        txtSpecialite.clear();
        if (lblErreur != null) lblErreur.setText("");

        basculerVersFormulaire();
    }

    @FXML
    private void handleModifier() {
        Medecin selection = medecinTable.getSelectionModel().getSelectedItem();
        if (selection != null) {
            this.medecinEnEdition = selection;
            lblTitreForm.setText("Modifier le Médecin");
            txtNom.setText(selection.getNom());
            txtPrenom.setText(selection.getPrenom());
            txtHoraire.setText(String.valueOf(selection.getTarifHoraire()));
            txtTelephone.setText(selection.getTelephone());
            txtEmail.setText(selection.getEmail());
            txtSpecialite.setText(selection.getSpecialite() != null ? selection.getSpecialite().name() : "");
            if (lblErreur != null) lblErreur.setText("");

            basculerVersFormulaire();
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtHoraire.getText().isEmpty() || txtSpecialite.getText().isEmpty()) {
            if (lblErreur != null) {
                lblErreur.setText("Erreur : Veuillez remplir tous les champs obligatoires (Nom, Prénom, Tarif et Spécialité).");
            }
            return;
        }

        try {
            double tarifHoraire = Double.parseDouble(txtHoraire.getText().replace(",", "."));
            String specSaisie = txtSpecialite.getText().toUpperCase().trim();
            Specialite spec = Specialite.valueOf(specSaisie);

            if (medecinEnEdition == null) {
                String numOrdreFictif = "ORD-" + Math.abs(txtNom.getText().hashCode());
                Medecin nouveau = new Medecin(
                        txtNom.getText(),
                        txtPrenom.getText(),
                        txtTelephone.getText(),
                        txtEmail.getText(),
                        numOrdreFictif,
                        spec,
                        tarifHoraire
                );
                gestionMedecins.ajouterMedecin(nouveau);
            } else {
                gestionMedecins.mettreAJourMedecin(
                        medecinEnEdition.getId(),
                        txtNom.getText(),
                        txtPrenom.getText(),
                        tarifHoraire,
                        txtTelephone.getText(),
                        txtEmail.getText(),
                        spec
                );
            }

            if (lblErreur != null) lblErreur.setText("");
            rafraichirTable();
            basculerVersDetails();
            medecinTable.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            if (lblErreur != null) {
                lblErreur.setText("Erreur : Le format du tarif horaire est invalide (ex: 35.50).");
            }
        } catch (IllegalArgumentException e) {
            if (lblErreur != null) {
                lblErreur.setText("Erreur : Cette spécialité n'existe pas dans la liste autorisée.");
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