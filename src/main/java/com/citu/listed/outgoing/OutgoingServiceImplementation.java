package com.citu.listed.outgoing;

import com.citu.listed.exception.NotFoundException;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.product.Product;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutgoingServiceImplementation implements OutgoingService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final IncomingRepository incomingRepository;
    private final JwtService jwtService;
    private final OutProductRepository outProductRepository;
    private final OutProductResponseMapper outProductResponseMapper;
    private final OutgoingRepository outgoingRepository;
    private final OutgoingResponseMapper outgoingResponseMapper;

    @Override
    @Transactional
    public OutgoingResponse outProducts(String token, Outgoing outgoing) {

        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        List<OutProduct> outProducts = new ArrayList<>();

        Double totalPrice = 0.0;

        for (OutProduct outProduct:outgoing.getProducts()) {

            if (outProduct.getQuantity() > incomingRepository.getTotalQuantityByProductId(outProduct.getProduct().getId()))
                throw new NotFoundException("Insufficient stock.");

            Product product = productRepository.findById(outProduct.getProduct().getId())
               .orElseThrow(() -> new NotFoundException("Product not found."));

            Double price = calculatePrice(outProduct.getQuantity(), product.getSalePrice());

            OutProduct newOutProduct = OutProduct.builder()
                .product(product)
                .quantity(outProduct.getQuantity())
                .price(price)
                .build();

            outProducts.add(outProductRepository.save(newOutProduct));
            totalPrice += price;
        }

        Outgoing newOutgoing = Outgoing.builder()
                .user(user)
                .products(outProducts)
                .category(outgoing.getCategory())
                .transactionDate(LocalDateTime.now())
                .comment(outgoing.getComment())
                .price(totalPrice)
                .build();

        outgoingRepository.save(newOutgoing);

        return outgoingResponseMapper.apply(newOutgoing);
    }

    private double calculatePrice(Double quantity, Double price){
        return quantity * price;
    }
}
