package com.mycoolcar.services;

import com.mycoolcar.entities.Role;
import com.mycoolcar.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
