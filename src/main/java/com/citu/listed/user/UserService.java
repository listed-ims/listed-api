package com.citu.listed.user;

import com.citu.listed.user.dtos.AuthenticationRequest;
import com.citu.listed.user.dtos.AuthenticationResponse;
import com.citu.listed.user.dtos.UserRequest;
import com.citu.listed.user.dtos.UserResponse;

public interface UserService {

   AuthenticationResponse register(UserRequest request);
   AuthenticationResponse authenticate(AuthenticationRequest request);
   boolean validateUsername(String username);
   UserResponse getUser(String token);
   UserResponse updateUser(String token, UserRequest request);
   boolean validatePassword(String password,String token);
   boolean validateToken(String token);
}
