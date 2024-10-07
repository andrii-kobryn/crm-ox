package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
class ContactRepositoryIntegrationTest {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client testClient;

    @BeforeEach
    public void setUp() {
        testClient = new Client();
        testClient.setCompanyName("Test Company");
        testClient.setIndustry("Software");
        testClient.setAddress("123 Software St");
        clientRepository.save(testClient);

        Contact contact1 = new Contact();
        contact1.setClient(testClient);
        contact1.setFirstName("John");
        contact1.setLastName("Doe");
        contactRepository.save(contact1);

        Contact contact2 = new Contact();
        contact2.setClient(testClient);
        contact2.setFirstName("Jane");
        contact2.setLastName("Smith");
        contactRepository.save(contact2);
    }

    @Test
    public void testFindByClientId() {
        List<Contact> result = contactRepository.findByClientId(testClient.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Contact::getFirstName).containsExactlyInAnyOrder("John", "Jane");
    }

    @Test
    public void testDeleteAllByClientId() {
        List<Contact> beforeDelete = contactRepository.findByClientId(testClient.getId());
        assertThat(beforeDelete).hasSize(2);

        contactRepository.deleteAllByClientId(testClient.getId());

        List<Contact> afterDelete = contactRepository.findByClientId(testClient.getId());
        assertThat(afterDelete).isEmpty();
    }

    @Test
    public void testFindContactsByClientId() {
        Set<Contact> result = contactRepository.findContactsByClientId(testClient.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Contact::getLastName).containsExactlyInAnyOrder("Doe", "Smith");
    }
}