package com.akobryn.crm.service.impl;

import com.akobryn.crm.entity.CRMUser;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.repository.CRMUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CRMUserRepository crmUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CRMUser crmUser = crmUserRepository.findByUsername(username)
                .orElseThrow(() -> CRMExceptions.userNotFound(username));

        return new User(
                crmUser.getUsername(),
                crmUser.getPassword(),
                Collections.singleton(crmUser.getRole()));
    }
}
