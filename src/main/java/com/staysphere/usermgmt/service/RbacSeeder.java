
package com.staysphere.usermgmt.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.staysphere.usermgmt.entity.Permission;
import com.staysphere.usermgmt.entity.Role;
import com.staysphere.usermgmt.repository.PermissionRepository;
import com.staysphere.usermgmt.repository.RoleRepository;

@Component
public class RbacSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RbacSeeder(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        Map<String, Permission> permissions = new LinkedHashMap<>();

        List<String> permissionNames = List.of(
            "USER_CREATE",
            "USER_UPDATE",
            "USER_ACTIVATE",
            "PROPERTY_MANAGE",
            "ROOM_VIEW",
            "ROOM_ALLOCATE",
            "CHECK_IN",
            "CHECK_OUT",
            "PAYMENT_VIEW",
            "PAYMENT_MANAGE",
            "COMPLAINT_CREATE",
            "COMPLAINT_MANAGE",
            "RESIDENT_PROFILE_VIEW",
            "AGREEMENT_VIEW"
        );

        // Create permissions only if they don't already exist
        for (String name : permissionNames) {
            Permission permission = permissionRepository
                .findByPermissionName(name)
                .orElseGet(() ->
                    permissionRepository.save(new Permission(name))
                );

            permissions.put(name, permission);
        }

        // Company Admin gets all permissions
        seedRole("COMPANY_ADMIN", permissions.values());

        // Staff
        seedRole("STAFF", List.of(
            permissions.get("ROOM_VIEW"),
            permissions.get("ROOM_ALLOCATE"),
            permissions.get("CHECK_IN"),
            permissions.get("CHECK_OUT"),
            permissions.get("COMPLAINT_MANAGE")
        ));

        // Accountant
        seedRole("ACCOUNTANT", List.of(
            permissions.get("PAYMENT_VIEW"),
            permissions.get("PAYMENT_MANAGE")
        ));

        // Resident
        seedRole("RESIDENT", List.of(
            permissions.get("RESIDENT_PROFILE_VIEW"),
            permissions.get("PAYMENT_VIEW"),
            permissions.get("AGREEMENT_VIEW"),
            permissions.get("COMPLAINT_CREATE")
        ));

        System.out.println("RBAC roles and permissions initialized.");
    }

    private void seedRole(
            String roleName,
            Collection<Permission> permissions) {

        Role role = roleRepository.findByRoleName(roleName)
            .orElseGet(() -> new Role(roleName));

        for (Permission permission : permissions) {
            role.addPermission(permission);
        }

        roleRepository.save(role);
    }
}
