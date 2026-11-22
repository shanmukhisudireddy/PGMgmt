package com.staysphere.usermgmt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staysphere.companymgmt.exception.InvalidStatusUpdateException;
import com.staysphere.usermgmt.dto.UserAccountStatusUpdate;
import com.staysphere.usermgmt.dto.UserRegistrationRequest;
import com.staysphere.usermgmt.dto.UserRoleUpdateRequest;
import com.staysphere.usermgmt.dto.UserUpdateRequest;
import com.staysphere.usermgmt.entity.User;
import com.staysphere.usermgmt.entity.Role;
import com.staysphere.usermgmt.enums.AccountStatusEnum;
import com.staysphere.usermgmt.exception.UserNotFoundException;
import com.staysphere.usermgmt.repository.RoleRepository;
import com.staysphere.usermgmt.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.staysphere.usermgmt.exception.EmailIdExistsException;

import java.util.Optional;
import java.util.List;
import java.util.Set;

@Service 
public class UserService {

    private final UserRepository userRepository;

    protected final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User findById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(()->new UserNotFoundException("User doesn't exists with user id "+ userId));
        
    }
    
    public void existsByEmailId(String emailId){
        if(userRepository.existsByEmailId(emailId)){
            throw new EmailIdExistsException("Email already exists for email Id "+emailId);
        }
    }

    public User findByEmailId(String emailId){
        Optional<User> user = userRepository.findByEmailId(emailId);
        return user.orElseThrow(()->new UserNotFoundException("User doesn't exists with email id "+ emailId));
    }

    public String generatePasswordHash(String password){
        return passwordEncoder.encode(password);
    }

    public boolean checkPasswordMatch(String password, String passwordHash){
        return passwordEncoder.matches(password, passwordHash);
    }

    public User registerUser(UserRegistrationRequest request){
        String emailId = request.getEmailId();
        String fullName = request.getFullName();
        String password = request.getPassword();

        existsByEmailId(emailId);
        User user = new User();
        user.setEmailId(emailId);
        user.setFullName(fullName);
        user.setPasswordHash(generatePasswordHash(password));
        user = userRepository.save(user);
        return user;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User updateDetails(UserUpdateRequest request, Long id){
        String emailId = request.getEmailId();
        String fullName = request.getFullName();
        User user = findById(id);
        user.setFullName(fullName);
        if(!emailId.equals(user.getEmailId())){
            existsByEmailId(emailId);
            user.setEmailId(emailId);
        }
        userRepository.save(user);
        return user;
    }

    public User updateStatus(Long id, UserAccountStatusUpdate request){
        User user = findById(id);
        AccountStatusEnum status = user.getAccountStatus();
        AccountStatusEnum accountStatus = request.getAccountStatus();
        if(accountStatus == AccountStatusEnum.ACTIVE && (status == AccountStatusEnum.PENDING
        || status == AccountStatusEnum.DISABLED)){
            user.setAccountStatus(accountStatus);
            userRepository.save(user);
        }
        else{
            String msg = "User status updation is not allowed from "+status+ " to "+ accountStatus;
            throw new InvalidStatusUpdateException(msg);
        }
        return user;
    }

    public User deactivateUser(Long id){
        User user = findById(id);
        AccountStatusEnum status = user.getAccountStatus();
        if(status == AccountStatusEnum.ACTIVE){
            user.setAccountStatus(AccountStatusEnum.DISABLED);
            userRepository.save(user);
        }
        else{
            String msg = "User status updation is not allowed from "+status+ " to "+ AccountStatusEnum.DISABLED;
            throw new InvalidStatusUpdateException(msg);
        }
        return user;
    }

    public User validateUser(UserRoleUpdateRequest request){
        User user = userRepository.findByEmailId(request.getEmailId()).orElseThrow(() -> new UserNotFoundException("Email not found"));
        if (!user.getFullName().trim()
            .equalsIgnoreCase(request.getFullName().trim())) {
        throw new IllegalArgumentException(
            "Full name does not match the registered user"
        );
    }

        if(user.getAccountStatus() != AccountStatusEnum.ACTIVE){
            throw new IllegalStateException(
            "Roles can only be assigned to active users"
        );
        }

        return user;
    }
    @Transactional 
    public User updateUserRoles(UserRoleUpdateRequest request){
        User user = validateUser(request);
        Set<Role> newRoles = request.getRoles().stream()
        .map(name -> roleRepository.findByRoleName(name.trim().toUpperCase())
        .orElseThrow(()-> new IllegalArgumentException("No roles received to udpate")))
        .collect(java.util.stream.Collectors.toSet());

    for (Role role : newRoles) {
        user.assignRoles(role);
    }

    return userRepository.save(user);

    }

    @Transactional
    public User deleteUserRoles(UserRoleUpdateRequest request) {

    User user = validateUser(request);

    Set<String> rolesToDelete = request.getRoles().stream()
        .map(name -> name.trim().toUpperCase())
        .collect(java.util.stream.Collectors.toSet());

    for (Role role : new java.util.HashSet<>(user.getRoles())) {
        if (rolesToDelete.contains(role.getRoleName())) {
            user.deleteRoles(role);
        }
    }

    return userRepository.save(user);
}
    }
