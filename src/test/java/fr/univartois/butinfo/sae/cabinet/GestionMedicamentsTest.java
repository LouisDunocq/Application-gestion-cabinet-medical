package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;

import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GestionMedicamentsTest {

    private GestionMedicaments gestionMedicaments;

    @Mock
    private Medicament medicament1;

    @Mock
    private Medicament medicament2;

    @BeforeEach
    void setUp() {
        gestionMedicaments = new GestionMedicaments();
    }



    @Test
    void testAjouterMedicamentSucces() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        gestionMedicaments.ajouterMedicament(medicament1);
        assertThat(gestionMedicaments.getNombreMedicaments()).isEqualTo(1);
    }

    @Test
    void testAjouterMedicamentDoublonRefuse() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        gestionMedicaments.ajouterMedicament(medicament1);
        Medicament doublon = mock(Medicament.class);
        gestionMedicaments.ajouterMedicament(medicament1);
        gestionMedicaments.ajouterMedicament(medicament1);
        assertThat(gestionMedicaments.getNombreMedicaments()).isEqualTo(1);
    }


    @Test
    void testRechercherParIdExistant() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        when(medicament1.getId()).thenReturn(42);
        gestionMedicaments.ajouterMedicament(medicament1);
        Medicament trouve = gestionMedicaments.rechercherParId(42);
        assertThat(trouve).isNotNull();
        assertThat(trouve).isEqualTo(medicament1);
    }

    @Test
    void testRechercherParIdInexistantRetourneNull() {
        Medicament trouve = gestionMedicaments.rechercherParId(999);
        assertThat(trouve).isNull();
    }


    @Test
    void testRechercherParNomTrouve() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        when(medicament2.getNom()).thenReturn("Ibuprofène");
        gestionMedicaments.ajouterMedicament(medicament1);
        gestionMedicaments.ajouterMedicament(medicament2);
        List<Medicament> resultats = gestionMedicaments.rechercherParNom("doli");
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0).getNom()).isEqualTo("Doliprane");
    }

    @Test
    void testRechercherParNomInsensibleLaCasse() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        gestionMedicaments.ajouterMedicament(medicament1);
        List<Medicament> resultats = gestionMedicaments.rechercherParNom("DOLIPRANE");
        assertThat(resultats).hasSize(1);
    }

    @Test
    void testRechercherParNomAucunResultat() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        gestionMedicaments.ajouterMedicament(medicament1);
        List<Medicament> resultats = gestionMedicaments.rechercherParNom("Xanax");
        assertThat(resultats).isEmpty();
    }


    @Test
    void testSupprimerMedicamentExistant() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        when(medicament1.getId()).thenReturn(10);
        gestionMedicaments.ajouterMedicament(medicament1);
        boolean supprime = gestionMedicaments.supprimerMedicament(10);
        assertThat(supprime).isTrue();
        assertThat(gestionMedicaments.getNombreMedicaments()).isEqualTo(0);
    }

    @Test
    void testSupprimerMedicamentInexistantRetourneFalse() {
        boolean supprime = gestionMedicaments.supprimerMedicament(999);
        assertThat(supprime).isFalse();
    }


    @Test
    void testListerTousLesMedicaments() {
        when(medicament1.getNom()).thenReturn("Doliprane");
        when(medicament2.getNom()).thenReturn("Ibuprofène");
        gestionMedicaments.ajouterMedicament(medicament1);
        gestionMedicaments.ajouterMedicament(medicament2);
        List<Medicament> liste = gestionMedicaments.listerTousLesMedicaments();
        assertThat(liste).hasSize(2).containsExactlyInAnyOrder(medicament1, medicament2);
    }

    @Test
    void testGetNombreMedicaments() {
        assertThat(gestionMedicaments.getNombreMedicaments()).isEqualTo(0);
        when(medicament1.getNom()).thenReturn("Doliprane");
        gestionMedicaments.ajouterMedicament(medicament1);
        assertThat(gestionMedicaments.getNombreMedicaments()).isEqualTo(1);
    }
}

