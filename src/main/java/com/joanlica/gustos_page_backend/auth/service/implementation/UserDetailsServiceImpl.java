package com.joanlica.gustos_page_backend.auth.service.implementation;

import com.joanlica.gustos_page_backend.auth.model.UserAuth;
import com.joanlica.gustos_page_backend.auth.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAuthRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Tenemos UserAuth (modelo) y User (Spring Security)

        UserAuth userAuth = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found."));

        // Con GrantedAuthority Spring Security maneja permisos
        Set<GrantedAuthority> authorityList = new LinkedHashSet<>();

        // Tomamos roles y los convertimos en SimpleGrantedAuthority para poder agregarlos a la authorityList
        userAuth.getRolesList()
                .forEach(role ->{
                    String roleName = role.getRoleName();
                    String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_".concat(roleName);
                    authorityList.add(new SimpleGrantedAuthority(authority));
                });

        // Retornamos el usuario en formato Spring Security con los datos de nuestro userAuth.
        // User es una implementación de UserDetails
        return new User(userAuth.getUsername(),
                userAuth.getPassword(),
                userAuth.isEnabled(),
                userAuth.isAccountNonExpired(),
                userAuth.isCredentialsNonExpired(),
                userAuth.isAccountNonLocked(),
                authorityList);
    }
}