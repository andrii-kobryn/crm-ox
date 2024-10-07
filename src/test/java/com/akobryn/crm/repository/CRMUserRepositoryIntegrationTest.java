package com.akobryn.crm.repository;

import com.akobryn.crm.entity.CRMUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
class CRMUserRepositoryIntegrationTest {
    @Autowired
    private CRMUserRepository crmUserRepository;

    private CRMUser testUser;

    @BeforeEach
    public void setUp() {
        testUser = new CRMUser();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        crmUserRepository.save(testUser);
    }

    @Test
    public void testFindByUsername_UserExists() {
        Optional<CRMUser> result = crmUserRepository.findByUsername("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsername_UserDoesNotExist() {
        Optional<CRMUser> result = crmUserRepository.findByUsername("nonexistentuser");

        assertThat(result).isNotPresent();
    }
}