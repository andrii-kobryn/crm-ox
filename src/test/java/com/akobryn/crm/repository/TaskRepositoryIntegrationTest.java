package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
class TaskRepositoryIntegrationTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ContactRepository contactRepository;

    private Client testClient;
    private Contact testContact;
    private Task testTask;

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

        testTask = new Task();
        testTask.setContact(testContact);
        testTask.setDescription("Test task description");
        testTask.setDueDate(LocalDate.now().plusDays(1)); // Завдання на завтра
        taskRepository.save(testTask);
    }

    @Test
    public void testFindByContactId_ExistingContact() {
        List<Task> result = taskRepository.findByContactId(testContact.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContact()).isEqualTo(testContact);
    }

    @Test
    public void testFindByContactId_NonExistingContact() {
        List<Task> result = taskRepository.findByContactId(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByContactClientId_ExistingClient() {
        List<Task> result = taskRepository.findByContactClientId(testClient.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContact().getClient()).isEqualTo(testClient);
    }

    @Test
    public void testFindByContactClientId_NonExistingClient() {
        List<Task> result = taskRepository.findByContactClientId(-1L);

        assertThat(result).isEmpty();
    }

    @Test
    public void testDeleteByContactId_ExistingContact() {
        List<Task> tasksBeforeDelete = taskRepository.findByContactId(testContact.getId());
        assertThat(tasksBeforeDelete).hasSize(1);

        taskRepository.deleteByContactId(testContact.getId());

        List<Task> tasksAfterDelete = taskRepository.findByContactId(testContact.getId());
        assertThat(tasksAfterDelete).isEmpty();
    }

    @Test
    public void testFindTasksDueTomorrow() {
        List<Task> result = taskRepository.findTasksDueTomorrow(LocalDate.now().plusDays(1));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDueDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    public void testFindTasksDueTomorrow_NoTasksDue() {
        List<Task> result = taskRepository.findTasksDueTomorrow(LocalDate.now().plusDays(3)); // Неіснуючий термін

        assertThat(result).isEmpty();
    }
}