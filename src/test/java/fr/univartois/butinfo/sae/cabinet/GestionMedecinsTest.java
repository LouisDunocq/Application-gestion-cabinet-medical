package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class GestionMedecinsTest {
	
    private GestionMedecins gestionMedecins;
    
	@Mock
    private Medecin dr1;
	
	@Mock
    private Medecin dr2;
	
	@Mock
    private Medecin dr3;

    @BeforeEach
    void setUp() {
        gestionMedecins = new GestionMedecins();
        dr1 = new Medecin("Dupont", "Jean", "0312345678", "j.dupont@cabinet.fr","ORD-001", Specialite.GENERALISTE, 30.0);
        dr2 = new Medecin("Martin", "Sophie", "0398765432", "s.martin@cabinet.fr","ORD-002", Specialite.CARDIOLOGUE, 50.0);
        dr3 = new Medecin("Bernard", "Lucie", "0367891234", "l.bernard@cabinet.fr","ORD-003", Specialite.PEDIATRE, 40.0);
    }


    @Test
    void testAjouterMedecinNouveauSucces() {
        gestionMedecins.ajouterMedecin(dr1);
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(1);
    }

    @Test
    void testAjouterMedecinDoublonNumeroOrdre() {
        gestionMedecins.ajouterMedecin(dr1);
        Medecin doublon = new Medecin("Autre", "Nom", "0600000000", "autre@mail.fr","ORD-001", Specialite.CARDIOLOGUE, 60.0);
        gestionMedecins.ajouterMedecin(doublon);
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(1);
    }

    @Test
    void testAjouterPlusieursMedecins() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.ajouterMedecin(dr2);
        gestionMedecins.ajouterMedecin(dr3);
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(3);
    }


    @Test
    void testRechercherParIdExistant() {
        gestionMedecins.ajouterMedecin(dr1);
        Medecin trouve = gestionMedecins.rechercherParId(dr1.getId());
        assertThat(trouve).isEqualTo(dr1);
    }

    @Test
    void testRechercherParIdInexistantRetourneNull() {
        Medecin trouve = gestionMedecins.rechercherParId(9999);
        assertThat(trouve).isNull();
    }


    @Test
    void testRechercherParNumeroOrdreExistant() {
        gestionMedecins.ajouterMedecin(dr1);
        Medecin trouve = gestionMedecins.rechercherParNumeroOrdre("ORD-001");
        assertThat(trouve).isEqualTo(dr1);
    }

    @Test
    void testRechercherParNumeroOrdreInsensibleCasse() {
        gestionMedecins.ajouterMedecin(dr1);
        Medecin trouve = gestionMedecins.rechercherParNumeroOrdre("ord-001");
        assertThat(trouve).isNotNull();
    }

    @Test
    void testRechercherParNumeroOrdreInexistantRetourneNull() {
        Medecin trouve = gestionMedecins.rechercherParNumeroOrdre("ORD-999");
        assertThat(trouve).isNull();
    }


    @Test
    void testRechercherParNomExistant() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.ajouterMedecin(dr2);
        List<Medecin> resultats = gestionMedecins.rechercherParNom("dupont");
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0)).isEqualTo(dr1);
    }

    @Test
    void testRechercherParNomParPrenom() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.ajouterMedecin(dr2);
        List<Medecin> resultats = gestionMedecins.rechercherParNom("sophie");
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0)).isEqualTo(dr2);
    }

    @Test
    void testRechercherParNomInsensibleCasse() {
        gestionMedecins.ajouterMedecin(dr1);
        List<Medecin> resultats = gestionMedecins.rechercherParNom("DUPONT");
        assertThat(resultats).hasSize(1);
    }

    @Test
    void testRechercherParNomAucunResultat() {
        gestionMedecins.ajouterMedecin(dr1);
        List<Medecin> resultats = gestionMedecins.rechercherParNom("Inexistant");
        assertThat(resultats).isEmpty();
    }


    @Test
    void testRechercherParSpecialiteExistante() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.ajouterMedecin(dr2);
        List<Medecin> resultats = gestionMedecins.rechercherParSpecialite(Specialite.CARDIOLOGUE);
        assertThat(resultats).hasSize(1);
        assertThat(resultats.get(0)).isEqualTo(dr2);
    }

    @Test
    void testRechercherParSpecialiteMultiple() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.ajouterMedecin(dr2);
        Medecin autreCardio = new Medecin("Petit", "Marc", "0300000000", "m.petit@cabinet.fr","ORD-004", Specialite.CARDIOLOGUE, 55.0);
        gestionMedecins.ajouterMedecin(autreCardio);
        List<Medecin> resultats = gestionMedecins.rechercherParSpecialite(Specialite.CARDIOLOGUE);
        assertThat(resultats).hasSize(2);
    }

    @Test
    void testRechercherParSpecialiteAucunResultat() {
        gestionMedecins.ajouterMedecin(dr1);
        List<Medecin> resultats = gestionMedecins.rechercherParSpecialite(Specialite.RADIOLOGUE);
        assertThat(resultats).isEmpty();
    }


    @Test
    void testMettreAJourMedecinExistant() {
        gestionMedecins.ajouterMedecin(dr1);
        gestionMedecins.mettreAJourMedecin(dr1.getId(),"Dupont", "Jean-Pierre", 35.0, "0312345679","jp.dupont@cabinet.fr", Specialite.GENERALISTE);
        Medecin modifie = gestionMedecins.rechercherParId(dr1.getId());
        assertThat(modifie.getPrenom()).isEqualTo("Jean-Pierre");
        assertThat(modifie.getTarifHoraire()).isEqualTo(35.0);
        assertThat(modifie.getTelephone()).isEqualTo("0312345679");
        assertThat(modifie.getEmail()).isEqualTo("jp.dupont@cabinet.fr");
    }

    @Test
    void testMettreAJourMedecinInexistantNeFaitRien() {
        assertThatCode(() ->
                gestionMedecins.mettreAJourMedecin(9999,"Nom", "Prenom", 30.0,"0600000000", "mail@test.fr", Specialite.GENERALISTE)
        ).doesNotThrowAnyException();
    }


    @Test
    void testSupprimerMedecinExistant() {
        gestionMedecins.ajouterMedecin(dr1);
        boolean result = gestionMedecins.supprimerMedecin(dr1.getId());
        assertThat(result).isTrue();
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(0);
    }

    @Test
    void testSupprimerMedecinInexistantRetourneFaux() {
        boolean result = gestionMedecins.supprimerMedecin(9999);
        assertThat(result).isFalse();
    }


    @Test
    void testListerTousLesMedecinsVide() {
        assertThat(gestionMedecins.listerTousLesMedecins()).isEmpty();
    }

    @Test
    void testListerTousLesMedecinsRetourneCopie() {
        gestionMedecins.ajouterMedecin(dr1);
        List<Medecin> liste = gestionMedecins.listerTousLesMedecins();
        liste.clear();
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(1);
    }

    @Test
    void testGetNombreMedecins() {
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(0);
        gestionMedecins.ajouterMedecin(dr1);
        assertThat(gestionMedecins.getNombreMedecins()).isEqualTo(1);
    }
}