package com.citu.listed.outgoing;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.outgoing.dtos.OutProductRequest;
import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import com.citu.listed.outgoing.mappers.OutgoingResponseMapper;
import com.citu.listed.product.Product;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.shared.exception.NotFoundException;
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
    private final OutgoingRepository outgoingRepository;
    private final OutgoingResponseMapper outgoingResponseMapper;

    @Override
    @Transactional
    public OutgoingResponse outProducts(String token, OutgoingRequest request) {

        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        List<OutProduct> outProducts = new ArrayList<>();

        Double totalPrice = 0.0;

        for (OutProductRequest outProduct:request.getProducts()) {

            if (outProduct.getQuantity() > incomingRepository.getTotalQuantityByProductId(outProduct.getProduct().getId()))
                throw new NotFoundException("Insufficient stock.");

            Product product = productRepository.findById(outProduct.getProduct().getId())
               .orElseThrow(() -> new NotFoundException("Product not found."));

            Double quantity = outProduct.getQuantity();

            Double price = calculatePrice(quantity, product.getSalePrice());

            OutProduct newOutProduct = OutProduct.builder()
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();

            outProducts.add(outProductRepository.save(newOutProduct));
            totalPrice += price;

            while (quantity > 0.0){
                Incoming incoming = incomingRepository.getEarliestByProductId(product.getId())
                        .orElseThrow(() -> new NotFoundException("No transaction found."));

                Double actualQuantity = incoming.getActualQuantity();

                if(quantity >= actualQuantity) {
                    incoming.setActualQuantity(0.0);
                    quantity -= actualQuantity;
                } else {
                    incoming.setActualQuantity(actualQuantity - quantity);
                    quantity = 0.0;
                }
            }
        }

        Outgoing newOutgoing = Outgoing.builder()
                .user(user)
                .products(outProducts)
                .category(request.getCategory())
                .transactionDate(LocalDateTime.now())
                .comment(request.getComment())
                .price(totalPrice)
                .build();

        outgoingRepository.save(newOutgoing);

        return outgoingResponseMapper.apply(newOutgoing);
    }

    private double calculatePrice(Double quantity, Double price){
        return quantity * price;
    }
}
