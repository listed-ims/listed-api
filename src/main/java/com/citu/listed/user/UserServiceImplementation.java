package com.citu.listed.user;

import com.citu.listed.exception.NotFoundException;
import com.citu.listed.exception.BadRequestException;
import com.citu.listed.user.config.JwtService;
import lombok.RequiredArgsConstructor;
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
    private final UserResponseMapper userResponseMapper;

    public AuthenticationResponse register(UserRequest request){
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

    @Override
    public UserResponse updateUser(String token, UserRequest request) {

        String currentUsername = jwtService.extractUsername(token);

        User userToUpdate = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("User not found."));


        if (!request.getUsername().equals(currentUsername)) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new BadRequestException("Username is already taken.");
            }
            userToUpdate.setUsername(request.getUsername());
        }

        userToUpdate.setName(request.getName());
        userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(userToUpdate);

        return userResponseMapper.apply(userToUpdate);
    }

    public boolean validatePassword(String password,String token) {
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);

        if (username == null || !username.isEmpty()){

            return passwordEncoder.matches(password,user.get().getPassword());
        }
        return false;
        
    }

}
