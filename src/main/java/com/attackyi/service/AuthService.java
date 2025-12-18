package com.attackyi.service;

import com.attackyi.domain.Role;
import com.attackyi.domain.UserAccount;
import com.attackyi.dto.AuthDtos;
import com.attackyi.repository.RoleRepository;
import com.attackyi.repository.UserRepository;
import com.attackyi.security.CustomUserDetails;
import com.attackyi.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthDtos.TokenResponse login(AuthDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .collect(java.util.stream.Collectors.toSet());
        String token = jwtTokenProvider.createToken(principal.getUsername(), roles);
        AuthDtos.TokenResponse response = new AuthDtos.TokenResponse();
        response.setToken(token);
        response.setUsername(principal.getUsername());
        response.setRoles(roles);
        return response;
    }

    @Transactional
    public AuthDtos.TokenResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> roleCodes = request.getRoles() == null || request.getRoles().isEmpty()
                ? Set.of("USER") : request.getRoles();
        Set<Role> roles = new HashSet<>();
        for (String code : roleCodes) {
            Role role = roleRepository.findByCode(code)
                    .orElseGet(() -> roleRepository.save(buildRole(code)));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);

        AuthDtos.LoginRequest loginRequest = new AuthDtos.LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        return login(loginRequest);
    }

    private Role buildRole(String code) {
        Role role = new Role();
        role.setCode(code);
        role.setName(code.toLowerCase());
        role.setDescription("Auto-created role");
        return role;
    }
}
