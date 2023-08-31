package com.citu.listed.user;

public interface UserService {

   AuthenticationResponse register(UserRequest request);
   AuthenticationResponse authenticate(AuthenticationRequest request);
   boolean validateUsername(String username);
   UserResponse getUser(String token);
   UserResponse updateUser(String token, UserRequest request);


}
