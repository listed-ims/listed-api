package com.citu.listed.user;

import com.citu.listed.product.ProductResponse;

public interface UserService {

   AuthenticationResponse register(RegisterRequest request);
   AuthenticationResponse authenticate(AuthenticationRequest request);
   boolean validateUsername(String username);
   UserResponse getUser(String token);


}
