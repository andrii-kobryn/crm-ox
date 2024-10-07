package com.akobryn.crm.repository;

import com.akobryn.crm.entity.CRMUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CRMUserRepository extends JpaRepository<CRMUser, Long> {
    Optional<CRMUser> findByUsername(String username);
}
