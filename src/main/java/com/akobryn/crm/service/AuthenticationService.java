package com.akobryn.crm.service;

import com.akobryn.crm.configuration.JwtService;
import com.akobryn.crm.constants.Role;
import com.akobryn.crm.dto.auth.AuthenticationRequest;
import com.akobryn.crm.dto.auth.AuthenticationResponse;
import com.akobryn.crm.entity.CRMUser;
import com.akobryn.crm.exceptions.CRMExceptions;
import com.akobryn.crm.repository.CRMUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CRMUserRepository crmUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(AuthenticationRequest request) {
        String username = request.getUsername();
        crmUserRepository.findByUsername(username).ifPresent(user -> {
            throw CRMExceptions.userAlreadyExists(username);
        });
        CRMUser crmUser = CRMUser.builder()
                .username(username)
                .password(request.getPassword())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        crmUserRepository.save(crmUser);
        String jwtToken = jwtService.generateToken(crmUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        String username = request.getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()
                )
        );
        CRMUser crmUser = crmUserRepository.findByUsername(username)
                .orElseThrow(() -> CRMExceptions.userNotFound(username));
        String jwtToken = jwtService.generateToken(crmUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
