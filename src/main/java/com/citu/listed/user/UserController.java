package com.citu.listed.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ){
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/validation/username")
    public ResponseEntity<Object> validateUsername(@RequestParam String username){
        return new ResponseEntity<>(service.validateUsername(username), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUser(@RequestHeader HttpHeaders headers) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        UserResponse userResponse = service.getUser(token);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
