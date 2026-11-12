package com.staysphere.usermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staysphere.usermgmt.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmailId(String emailId);

    boolean existsByEmailId(String emailId);

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

}