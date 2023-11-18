package com.licenta.web;

import com.licenta.service.AuthenticationResponse;
import com.licenta.service.UserService;
import com.licenta.service.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.save(userDTO));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.login(userDTO));
    }
}
