package com.staysphere.usermgmt.service;

import com.staysphere.usermgmt.repository.RoleRepository;
import com.staysphere.usermgmt.repository.UserRepository;
import com.staysphere.usermgmt.dto.RoleRegistrationResponse;
import com.staysphere.usermgmt.dto.RoleResponse;
import com.staysphere.usermgmt.entity.Role;
import com.staysphere.usermgmt.entity.User;
import com.staysphere.usermgmt.entity.Permission;
import com.staysphere.usermgmt.exception.UserNotFoundException;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class RoleService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Role findByRoleName(String roleName){
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        return role.orElseThrow(()->new UserNotFoundException("Role doesn't exists with role name "+ roleName));
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(role -> new RoleResponse(
                        role.getRoleId(),
                        role.getRoleName(),
                        role.getPermissions()
                                .stream()
                                .map(Permission::getPermissionName)
                                .toList()
                ))
                .toList();
    }

     @Transactional(readOnly = true)
    public List<RoleResponse> getRolesForUser(String emailId) {

        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new UserNotFoundException(emailId));

        List<RoleResponse> roleResponses = user.getRoles()
                .stream()
                .map(role -> new RoleResponse(
                        role.getRoleId(),
                        role.getRoleName(),
                        role.getPermissions()
                                .stream()
                                .map(Permission::getPermissionName)
                                .toList()
                ))
                .toList();

        return roleResponses;
    }


}
