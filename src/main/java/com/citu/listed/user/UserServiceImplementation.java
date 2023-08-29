package com.citu.listed.user;

import com.citu.listed.exception.NotFoundException;
import com.citu.listed.exception.BadRequestException;
import com.citu.listed.user.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserResponseMapper userResponseMapper;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        if (validateUsername(user.getUsername())) {
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new BadRequestException("Username is already taken.");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean validateUsername(String username){
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public UserResponse getUser(String token) {

        String username = jwtService.extractUsername(token);

        Optional<User> user = userRepository.findByUsername(username);

        return user.map(userResponseMapper)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

}

