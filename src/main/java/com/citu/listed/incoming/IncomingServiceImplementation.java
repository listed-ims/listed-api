package com.citu.listed.incoming;


import com.citu.listed.exception.NotFoundException;
import com.citu.listed.product.Product;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.citu.listed.user.config.JwtService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class IncomingServiceImplementation implements IncomingService {

    private final IncomingRepository incomingRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final IncomingResponseMapper incomingResponseMapper;

    @Override
    public IncomingResponse inProduct(String token, Integer productId, IncomingRequest request) {


        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found."));



        Incoming newIncoming = Incoming.builder()
                .product(product)
                .user(user)
                .initialQuantity(request.getInitialQuantity())
                .actualQuantity(request.getInitialQuantity())
                .purchasePrice(request.getPurchasePrice())
                .expirationDate(request.getExpirationDate())
                .transactionDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .comment(request.getComment())
                .build();

       incomingRepository.save(newIncoming);

       return incomingResponseMapper.apply(newIncoming);

    }



}
