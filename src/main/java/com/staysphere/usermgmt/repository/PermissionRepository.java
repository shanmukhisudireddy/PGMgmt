package com.staysphere.usermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import  java.util.Optional;

import com.staysphere.usermgmt.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{
    Optional<Permission> findByPermissionName(String permissionName);

    boolean existsByPermissionName(String permissionName);
}
