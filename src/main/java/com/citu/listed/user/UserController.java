package com.citu.listed.user;

import com.citu.listed.shared.ValidationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid UserRequest request
    ){
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("users/validation/username")
    public ResponseEntity<Object> validateUsername(@RequestParam String username){
        return new ResponseEntity<>(
                new ValidationResponse(userService.validateUsername(username)),
                HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUser(@RequestHeader HttpHeaders headers) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        UserResponse userResponse = userService.getUser(token);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader HttpHeaders headers,
                                                   @RequestBody @Valid UserRequest request) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        UserResponse updatedUserResponse = userService.updateUser(token, request);

        return new ResponseEntity<>(updatedUserResponse, HttpStatus.OK);
    }




}

