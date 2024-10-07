package com.akobryn.crm.repository;

import com.akobryn.crm.entity.Contact;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByClientId(Long clientId);
    @Modifying
    @Query("DELETE FROM Contact c WHERE c.client.id = :clientId")
    void deleteAllByClientId(@Param("clientId") Long clientId);

    Set<Contact> findContactsByClientId(Long clientId);

}
