package com.akobryn.crm.service.impl;

import com.akobryn.crm.constants.TaskStatus;
import com.akobryn.crm.dto.TaskDTO;
import com.akobryn.crm.entity.Client;
import com.akobryn.crm.entity.Contact;
import com.akobryn.crm.entity.Task;
import com.akobryn.crm.mapper.TaskMapper;
import com.akobryn.crm.repository.ClientRepository;
import com.akobryn.crm.repository.ContactRepository;
import com.akobryn.crm.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) 
@Transactional
class TaskServiceIntegrationTest {
    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ClientRepository clientRepository;

    private Contact testContact;

    @BeforeEach
    public void setUp() {
        Client testClient = new Client();
        testClient.setAddress("Test address");
        testClient.setIndustry("Test industry");
        testClient.setCompanyName("Test name");
        clientRepository.save(testClient);

        testContact = new Contact();
        testContact.setFirstName("Test");
        testContact.setLastName("Test");
        testContact.setPhone("123");
        testContact.setClient(testClient);
        contactRepository.save(testContact);
    }

    @Test
    public void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("Test Task");
        taskDTO.setStatus(TaskStatus.OPEN);
        taskDTO.setContactId(testContact.getId());

        TaskDTO createdTask = taskService.createTask(taskDTO);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getDescription()).isEqualTo("Test Task");
        assertThat(createdTask.getStatus()).isEqualTo(TaskStatus.OPEN);

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getDescription()).isEqualTo("Test Task");
    }

    @Test
    public void testUpdateTaskStatus() {
        Task task = new Task();
        task.setDescription("Update Task");
        task.setStatus(TaskStatus.OPEN);
        task.setContact(testContact);
        taskRepository.save(task);

        TaskDTO updatedTask = taskService.updateTaskStatus(task.getId(), TaskStatus.COMPLETED);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    public void testSetContactId() {
        Task task = new Task();
        task.setDescription("Set Contact Task");
        task.setStatus(TaskStatus.OPEN);
        task.setContact(testContact);
        taskRepository.save(task);

        TaskDTO updatedTask = taskService.setContactId(task.getId(), testContact.getId());

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(task.getId());
        assertThat(updatedTask.getDescription()).isEqualTo("Set Contact Task");
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setDescription("Delete Task");
        task.setStatus(TaskStatus.OPEN);
        task.setContact(testContact);
        taskRepository.save(task);

        Long deletedTaskId = taskService.deleteTask(task.getId());

        assertThat(deletedTaskId).isEqualTo(task.getId());
        assertThat(taskRepository.findById(task.getId())).isEmpty(); 
    }

    @Test
    public void testFindTaskById() {
        Task task = new Task();
        task.setDescription("Find Task");
        task.setStatus(TaskStatus.OPEN);
        task.setContact(testContact);
        taskRepository.save(task);

        TaskDTO foundTask = taskService.findTaskById(task.getId());

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getDescription()).isEqualTo("Find Task");
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setStatus(TaskStatus.OPEN);
        task1.setContact(testContact);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setStatus(TaskStatus.COMPLETED);
        task2.setContact(testContact);
        taskRepository.save(task2);

        List<TaskDTO> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.stream().map(TaskDTO::getDescription).toList()).containsExactlyInAnyOrder("Task 1", "Task 2");
    }
}