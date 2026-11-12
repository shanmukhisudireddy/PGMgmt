package com.staysphere.usermgmt.repository;

import com.staysphere.usermgmt.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);
}
