package com.joanlica.gustos_page_backend.auth.service;

import com.joanlica.gustos_page_backend.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.gustos_page_backend.auth.dto.admin.UserRolesResponseDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.UserAuthResponseDTO;

public interface AdminAuthService {

    UserRolesResponseDTO updateRoleList(String username, UpdateUserRolesDTO updateUserRolesDTO);
}