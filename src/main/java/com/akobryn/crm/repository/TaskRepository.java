package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByContactId(Long contactId);
    List<Task> findByContactClientId(Long clientId);
    void deleteByContactId(Long contactId);
    @Query("SELECT t FROM Task t WHERE t.dueDate = :tomorrow")
    List<Task> findTasksDueTomorrow(LocalDate tomorrow);
}
