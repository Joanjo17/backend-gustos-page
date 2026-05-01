package com.joanlica.gustos_page_backend.config;

import com.joanlica.gustos_page_backend.auth.model.Role;
import com.joanlica.gustos_page_backend.auth.model.UserAuth;
import com.joanlica.gustos_page_backend.auth.repository.RoleRepository;
import com.joanlica.gustos_page_backend.auth.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserAuthRepository userAuthRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminUserInitializer(UserAuthRepository userAuthRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // --- 1. Crear Roles si no existen ---
        // Función para "buscar o crear"
        Role roleAdmin = findOrCreateRole("ADMIN");
        Role roleUser = findOrCreateRole("USER");

        // --- 2. Comprobar si el admin ya existe ---
        if (userAuthRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        // --- 3. Si no existe, crearlo ---
        System.out.println("Creando usuario administrador por defecto: " + adminUsername);
        UserAuth adminUser = new UserAuth();
        adminUser.setUsername(adminUsername);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setRolesList(Set.of(roleAdmin, roleUser));

        userAuthRepository.save(adminUser);
        System.out.println("¡Usuario administrador creado!");
    }

    /**
     * Helper para buscar un rol por su nombre.
     * Si no lo encuentra, lo crea y lo guarda.
     */
    private Role findOrCreateRole(String roleName) {
        // Busca el rol. Si no existe, crea uno nuevo.
        return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    System.out.println("Creando rol por defecto: " + roleName);
                    return roleRepository.save(new Role(null, roleName));
                });
    }
}