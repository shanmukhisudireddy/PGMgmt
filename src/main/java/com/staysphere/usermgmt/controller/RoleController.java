package com.staysphere.usermgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.staysphere.usermgmt.dto.GetAllRolesResponse;
import com.staysphere.usermgmt.dto.RoleRegistrationResponse;
import com.staysphere.usermgmt.dto.RoleResponse;
import com.staysphere.usermgmt.dto.UserRoleUpdateRequest;
import com.staysphere.usermgmt.service.RoleService;
import com.staysphere.usermgmt.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.util.List;

import com.staysphere.usermgmt.entity.User;

@RestController 
@RequestMapping ("/api/roles")
public class RoleController {
    private final RoleService roleService;

    private final UserService userService;
    public RoleController(UserService userService, RoleService roleService){
        this.roleService=roleService;
        this.userService=userService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserRoles(@RequestBody @Valid UserRoleUpdateRequest request) {
        userService.updateUserRoles(request);       
        return ResponseEntity.status(HttpStatus.OK).body("User Roles updated Successfully");
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteUserRoles(@RequestBody @Valid UserRoleUpdateRequest request) {
        userService.deleteUserRoles(request);       
        return ResponseEntity.status(HttpStatus.OK).body("User Roles updated Successfully");
    }

    @GetMapping("/getRole/{emailId}")
    public ResponseEntity<RoleRegistrationResponse> getRolesForUser(@PathVariable ("emailId") @Email String emailId) {
        List<RoleResponse> roles = roleService.getRolesForUser(emailId);
        User user = userService.findByEmailId(emailId);
        RoleRegistrationResponse response = new RoleRegistrationResponse(emailId, user.getFullName(), roles);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/allRoles")
    public ResponseEntity<GetAllRolesResponse> getAllRoles() {
        List<RoleResponse> roles = roleService.getAllRoles();
        GetAllRolesResponse response = new GetAllRolesResponse(roles);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
