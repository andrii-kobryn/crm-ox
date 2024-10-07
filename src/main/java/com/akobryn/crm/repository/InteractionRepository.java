package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByClientId(Long clientId);
    List<Interaction> findByContactIdAndClientId(Long contactId, Long clientId);
}
