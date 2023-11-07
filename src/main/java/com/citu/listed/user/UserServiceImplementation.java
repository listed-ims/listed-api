package com.citu.listed.user;

import com.citu.listed.membership.enums.MembershipStatus;
import com.citu.listed.shared.exception.BadRequestException;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import com.citu.listed.token.Token;
import com.citu.listed.token.TokenRepository;
import com.citu.listed.token.TokenType;
import com.citu.listed.user.config.JwtService;
import com.citu.listed.user.dtos.AuthenticationRequest;
import com.citu.listed.user.dtos.AuthenticationResponse;
import com.citu.listed.user.dtos.UserRequest;
import com.citu.listed.user.dtos.UserResponse;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserResponseMapper userResponseMapper;

    public AuthenticationResponse register(UserRequest request) {
        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        if (validateUsername(user.getUsername())) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            savedUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new BadRequestException("Username is already taken.");
        }
    }

    private void savedUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        var jwtToken = jwtService.generateToken(user);
        savedUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean validateUsername(String username) {
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

        if (request.getCurrentStoreId() == null) {
            if (!request.getUsername().equals(currentUsername)) {
                if (userRepository.findByUsername(request.getUsername()).isPresent())
                    throw new BadRequestException("Username is already taken.");
                else
                    userToUpdate.setUsername(request.getUsername());
            }

            userToUpdate.setName(request.getName());
            userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            Store store = storeRepository.findByIdAndMembersUserAndMembersMembershipStatusNot(
                            request.getCurrentStoreId(),
                            userToUpdate,
                            MembershipStatus.INACTIVE
                    )
                    .orElseThrow(() -> new NotFoundException("Store not found."));

            userToUpdate.setCurrentStoreId(store.getId());
        }

        return userResponseMapper.apply(userRepository.save(userToUpdate));
    }

    public boolean validatePassword(String password, String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean validateToken(String token) {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(jwtService.extractUsername(token));

        return jwtService.isTokenValid(token, userDetails);
    }

}
