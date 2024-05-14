package com.licenta.web;

import com.google.zxing.WriterException;
import com.licenta.domain.vo.UserVO;
import com.licenta.service.UserService;
import com.licenta.service.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final LoginValidator loginValidator;

    public UserController(UserService userService, UserValidator userValidator, LoginValidator loginValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.loginValidator = loginValidator;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> register(@RequestPart String userDTOString, @RequestPart(value = "file", required = false) MultipartFile file, BindingResult bindingResult) throws IOException, WriterException {
        @Valid UserDTO userDTO = userService.getDTOFromString(userDTOString);
        userValidator.validate(userDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                if (error instanceof FieldError) {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                } else {
                    errors.put("globalError", error.getDefaultMessage());
                }
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(userService.save(userDTO, file));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam("token") String token) {
        userService.confirmUser(token);
        return ResponseEntity.ok("Te-ai inregistrat cu succes!");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        loginValidator.validate(userDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getCode();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(userService.login(userDTO));
    }

    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<UserVO> getProfileInformation() {
        return ResponseEntity.ok(userService.getProfileInformation());
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<UserDTO> editProfileInformation(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.editProfileInformation(userDTO));
    }
}
