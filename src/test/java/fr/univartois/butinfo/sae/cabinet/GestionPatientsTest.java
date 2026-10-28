package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionPatientsTest {

    private GestionPatients gestionPatients;

    @Mock
    private Patient patient1;

    @Mock
    private Patient patient2;

    @BeforeEach
    void setUp() {
        gestionPatients = new GestionPatients();
    }

    @Test
    void testAjouterPatientNouveauSucces() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(patient1);
        assertThat(gestionPatients.getNombrePatients()).isEqualTo(1);
    }

    @Test
    void testAjouterPatientDoublonNumeroSecu() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(patient1);
        Patient doublon = mock(Patient.class);
        when(doublon.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(doublon);
        assertThat(gestionPatients.getNombrePatients()).isEqualTo(1);
    }
    
    @Test
    void testAjouterPlusieursPatients() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient2.getNumeroSecu()).thenReturn("295076210054321");
        gestionPatients.ajouterPatient(patient1);
        gestionPatients.ajouterPatient(patient2);
        assertThat(gestionPatients.getNombrePatients()).isEqualTo(2);
    }

    @Test
    void testRechercherParIdExistant() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getId()).thenReturn(1);
        gestionPatients.ajouterPatient(patient1);
        Patient trouve = gestionPatients.rechercherParId(1);
        assertThat(trouve).isNotNull();
        assertThat(trouve).isEqualTo(patient1);
    }

    @Test
    void testRechercherParIdInexistantRetourneNull() {
        Patient trouve = gestionPatients.rechercherParId(9999);
        assertThat(trouve).isNull();
    }

    @Test
    void testRechercherParNumeroSecuExistant() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(patient1);
        Patient trouve = gestionPatients.rechercherParNumeroSecu("185036562012345");
        assertThat(trouve).isEqualTo(patient1);
    }

    @Test
    void testRechercherParNumeroSecuInsensibleCasse() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(patient1);
        Patient trouve = gestionPatients.rechercherParNumeroSecu("185036562012345");
        assertThat(trouve).isNotNull();
    }

    @Test
    void testRechercherParNumeroSecuInexistantRetourneNull() {
        Patient trouve = gestionPatients.rechercherParNumeroSecu("000000000000000");
        assertThat(trouve).isNull();
    }

    @Test
    void testRechercherParNomTrouve() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getNom()).thenReturn("Leblanc");
        when(patient2.getNumeroSecu()).thenReturn("295076210054321");
        when(patient2.getNom()).thenReturn("Moreau");
        when(patient2.getPrenom()).thenReturn("Claire");
        gestionPatients.ajouterPatient(patient1);
        gestionPatients.ajouterPatient(patient2);
        List<Patient> resultats = gestionPatients.rechercherParNom("leblanc");
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0)).isEqualTo(patient1);
    }

    @Test
    void testRechercherParNomPartiel() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getNom()).thenReturn("Leblanc");
        when(patient1.getPrenom()).thenReturn("Paul");
        when(patient2.getNumeroSecu()).thenReturn("295076210054321");
        when(patient2.getNom()).thenReturn("Moreau");
        gestionPatients.ajouterPatient(patient1);
        gestionPatients.ajouterPatient(patient2);
        List<Patient> resultats = gestionPatients.rechercherParNom("eau");
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0)).isEqualTo(patient2);
    }

    @Test
    void testRechercherParNomInsensibleCasse() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getNom()).thenReturn("Leblanc");
        gestionPatients.ajouterPatient(patient1);
        List<Patient> resultats = gestionPatients.rechercherParNom("LEBLANC");
        assertThat(resultats).hasSize(1);
    }

    @Test
    void testRechercherParNomAucunResultat() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getNom()).thenReturn("Leblanc");
        when(patient1.getPrenom()).thenReturn("Paul");
        gestionPatients.ajouterPatient(patient1);
        List<Patient> resultats = gestionPatients.rechercherParNom("Inexistant");
        assertThat(resultats).isEmpty();
    }

    @Test
    void testRechercherParPrenomTrouve() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getNom()).thenReturn("Leblanc");
        when(patient1.getPrenom()).thenReturn("Paul");
        gestionPatients.ajouterPatient(patient1);
        List<Patient> resultats = gestionPatients.rechercherParNom("paul");
        assertThat(resultats).hasSize(1);
    }

    @Test
    void testMettreAJourPatientExistant() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getId()).thenReturn(1);
        gestionPatients.ajouterPatient(patient1);
        gestionPatients.mettreAJourPatient(1,"Leblanc", "Paul-Henri", "0699887766","paul.henri@mail.fr", "20 rue des Roses", 0.80);
        verify(patient1).setNom("Leblanc");
        verify(patient1).setPrenom("Paul-Henri");
        verify(patient1).setTelephone("0699887766");
        verify(patient1).setEmail("paul.henri@mail.fr");
        verify(patient1).setAdresse("20 rue des Roses");
        verify(patient1).setTauxRemboursement(0.80);
    }

    @Test
    void testMettreAJourPatientInexistantNeFaitRien() {
        assertThatCode(() -> gestionPatients.mettreAJourPatient(9999,"Nom", "Prenom", "0600000000","mail@test.fr", "Adresse", 0.75)).doesNotThrowAnyException();
    }

    @Test
    void testSupprimerPatientExistant() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        when(patient1.getId()).thenReturn(1);
        gestionPatients.ajouterPatient(patient1);
        gestionPatients.supprimerPatient(1);
        assertThat(gestionPatients.getNombrePatients()).isEqualTo(0);
    }

    @Test
    void testSupprimerPatientInexistantNeFaitRien() {
        when(patient1.getNumeroSecu()).thenReturn("185036562012345");
        gestionPatients.ajouterPatient(patient1);
        assertThatCode(() -> gestionPatients.supprimerPatient(9999)).doesNotThrowAnyException();
    }
}