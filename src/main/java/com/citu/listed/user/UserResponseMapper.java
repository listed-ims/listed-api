package com.citu.listed.user;

import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class UserResponseMapper implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getCurrentStoreId()
        );
    }
}
