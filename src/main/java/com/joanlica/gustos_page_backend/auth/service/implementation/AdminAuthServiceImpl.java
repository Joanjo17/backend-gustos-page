package com.joanlica.gustos_page_backend.auth.service.implementation;

import com.joanlica.gustos_page_backend.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.gustos_page_backend.auth.dto.admin.UserRolesResponseDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.gustos_page_backend.auth.model.Role;
import com.joanlica.gustos_page_backend.auth.repository.RoleRepository;
import com.joanlica.gustos_page_backend.auth.repository.UserAuthRepository;
import com.joanlica.gustos_page_backend.auth.service.AdminAuthService;
import com.joanlica.gustos_page_backend.core.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {

    private final UserAuthRepository userAuthRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserRolesResponseDTO updateRoleList(String username, UpdateUserRolesDTO updateUserRolesDTO) {
        var userAuth = userAuthRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var newRoles = updateUserRolesDTO.rolesList().stream()
                .map(roleName -> {
                    return roleRepository.findByRoleName(roleName)
                            .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));
                })
                .collect(Collectors.toSet());

        userAuth.setRolesList(newRoles);
        userAuthRepository.save(userAuth);

        return new UserRolesResponseDTO(userAuth.getId(),
                userAuth.getUsername(),
                updateUserRolesDTO.rolesList());
    }
}