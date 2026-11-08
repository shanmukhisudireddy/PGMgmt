package com.staysphere.usermgmt.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staysphere.tenantprovisioning.exception.InvalidStatusUpdateException;
import com.staysphere.usermgmt.dto.UserAccountStatusUpdate;
import com.staysphere.usermgmt.dto.UserRegistrationRequest;
import com.staysphere.usermgmt.dto.UserUpdateRequest;
import com.staysphere.usermgmt.entity.User;
import com.staysphere.usermgmt.enums.AccountStatusEnum;
import com.staysphere.usermgmt.exception.UserNotFoundException;
import com.staysphere.usermgmt.repository.UserRepository;

import com.staysphere.usermgmt.exception.EmailIdExistsException;

import java.util.Optional;
import java.util.List;

@Service 
public class UserService {

    private final UserRepository userRepository;

    protected final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        if(accountStatus == AccountStatusEnum.ACTIVE && status == AccountStatusEnum.PENDING){
            user.setAccountStatus(accountStatus);
            userRepository.save(user);
        }
        else{
            String msg = "User status updation is not allowed from "+status+ " to "+ accountStatus;
            throw new InvalidStatusUpdateException(msg);
        }
        return user;
    }
}
