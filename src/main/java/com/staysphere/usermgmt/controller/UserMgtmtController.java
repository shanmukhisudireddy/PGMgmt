package com.staysphere.usermgmt.controller;

import com.staysphere.usermgmt.entity.User;
import com.staysphere.usermgmt.service.UserService;
import com.staysphere.usermgmt.dto.UserAccountStatusUpdate;
import com.staysphere.usermgmt.dto.UserRegistrationRequest;
import com.staysphere.usermgmt.dto.UserRegistrationResponse;
import com.staysphere.usermgmt.dto.UserUpdateRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;


@RestController 
@RequestMapping ("/api/users")
public class UserMgtmtController {
    private final UserService userService;

    public UserMgtmtController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        User user = userService.registerUser(request);

        UserRegistrationResponse response = 
                        new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserRegistrationResponse> getUserById(@PathVariable("id")Long id) {
        User user = userService.findById(id);
        UserRegistrationResponse response = 
                        new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserRegistrationResponse>> getAllUsers() {
        List<User> userList = userService.getUsers();
        List<UserRegistrationResponse> response = new ArrayList<>();
        for (User user : userList) {
            response.add(new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserRegistrationResponse> updateDetails(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateRequest request) {
        User user = userService.updateDetails(request, id);
        UserRegistrationResponse response = new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping ("/{id}/status")
    public ResponseEntity<UserRegistrationResponse> updateStatus(@PathVariable ("id") Long id, @RequestBody @Valid UserAccountStatusUpdate request){
        User user = userService.updateStatus(id, request);
         UserRegistrationResponse response = new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }


    @PatchMapping("{id}/deactivate")
    public ResponseEntity<UserRegistrationResponse> deactivateUser(@PathVariable ("id") Long id){
        User user = userService.deactivateUser(id);
         UserRegistrationResponse response = new UserRegistrationResponse(user.getUserId(), 
                            user.getFullName(), user.getEmailId(), 
                            user.getAccountStatus(), user.getCreatedAt());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
