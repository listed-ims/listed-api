package com.citu.listed.user;

public interface UserService {

   AuthenticationResponse register(RegisterRequest request);
   AuthenticationResponse authenticate(AuthenticationRequest request);
   boolean validateUsername(String username);
}
