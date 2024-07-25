package com.mycoolcar.services;

import com.mycoolcar.entities.Role;
import com.mycoolcar.exceptions.ResourceNotFoundException;
import com.mycoolcar.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createNewRole(Role role) {
        log.info("Creating new role: {}", role.getRoleName());
        Role savedRole = roleRepository.save(role);
        log.info("Role created with ID: {}", savedRole.getId());
        return savedRole;
    }

    public Role findByRoleName(String roleName) {
        log.info("Finding role by name: {}", roleName);
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            log.warn("Role with name: {} not found", roleName);
            throw new ResourceNotFoundException("Role with name " + roleName + " not found");
        }
        log.info("Found role: {}", role.getRoleName());
        return role;
    }
}
