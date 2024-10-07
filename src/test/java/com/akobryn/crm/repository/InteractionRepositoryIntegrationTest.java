package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Interaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
class InteractionRepositoryIntegrationTest {
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    private Client testClient;
    private Contact testContact;
    private Interaction testInteraction;

    @BeforeEach
    public void setUp() {
        testClient = new Client();
        testClient.setCompanyName("Test Company");
        testClient.setIndustry("Software");
        testClient.setAddress("123 Software St");
        clientRepository.save(testClient);

        testContact = new Contact();
        testContact.setFirstName("John");
        testContact.setLastName("Doe");
        testContact.setClient(testClient);
        contactRepository.save(testContact);

        testInteraction = new Interaction();
        testInteraction.setClientId(testClient.getId());
        testInteraction.setContactId(testContact.getId());
        testInteraction.setMessage("Test interaction details");
        interactionRepository.save(testInteraction);
    }

    @Test
    public void testFindByClientId_ExistingClient() {
        List<Interaction> result = interactionRepository.findByClientId(testClient.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClientId()).isEqualTo(testClient.getId());
    }

    @Test
    public void testFindByClientId_NonExistingClient() {
        List<Interaction> result = interactionRepository.findByClientId(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByContactIdAndClientId_ExistingContactAndClient() {
        List<Interaction> result = interactionRepository.findByContactIdAndClientId(testContact.getId(), testClient.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContactId()).isEqualTo(testContact.getId());
        assertThat(result.get(0).getClientId()).isEqualTo(testClient.getId());
    }

    @Test
    public void testFindByContactIdAndClientId_NonExistingCombination() {
        List<Interaction> result = interactionRepository.findByContactIdAndClientId(-1L, -1L);

        assertThat(result).isEmpty();
    }
}