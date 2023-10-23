package com.citu.listed.outgoing;

import com.citu.listed.incoming.Incoming;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.notification.NotificationService;
import com.citu.listed.notification.enums.NotificationType;
import com.citu.listed.outgoing.dtos.OutProductRequest;
import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.outgoing.mappers.OutgoingResponseMapper;
import com.citu.listed.product.Product;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OutgoingServiceImplementation implements OutgoingService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final IncomingRepository incomingRepository;
    private final OutProductRepository outProductRepository;
    private final OutgoingRepository outgoingRepository;
    private final StoreRepository storeRepository;
    private final JwtService jwtService;
    private final OutgoingResponseMapper outgoingResponseMapper;
    private  final NotificationService notificationService;

    @Override
    @Transactional
    public OutgoingResponse outProducts(String token, OutgoingRequest request) {


        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        List<OutProduct> outProducts = new ArrayList<>();

        Double totalPrice = 0.0;
        Double totalRevenue = 0.0;

        for (OutProductRequest outProduct:request.getProducts()) {

            if (outProduct.getQuantity() > incomingRepository.getTotalQuantityByProductId(outProduct.getProduct().getId()))
                throw new NotFoundException("Insufficient stock.");

            Product product = productRepository.findById(outProduct.getProduct().getId())
               .orElseThrow(() -> new NotFoundException("Product not found."));

            Double quantity = outProduct.getQuantity();
            Double price = calculatePrice(quantity, product.getSalePrice());
            Double purchasePrice = 0.0;

            OutProduct newOutProduct = OutProduct.builder()
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();

            outProducts.add(outProductRepository.save(newOutProduct));
            totalPrice += price;

            while (quantity > 0.0){

                Incoming incoming;
                if(request.getCategory() == OutgoingCategory.EXPIRED) {
                    incoming = incomingRepository.findFirstByActualQuantityGreaterThanAndProductIdOrderByExpirationDateDesc(0.0, product.getId())
                            .orElseThrow(() -> new NotFoundException("No transaction found."));
                } else{
                    incoming = incomingRepository.findFirstByActualQuantityGreaterThanAndProductIdOrderByExpirationDateAsc(0.0, product.getId())
                            .orElseThrow(() -> new NotFoundException("No transaction found."));
                }

                Double actualQuantity = incoming.getActualQuantity();

                if(quantity >= actualQuantity) {
                    incoming.setActualQuantity(0.0);
                    purchasePrice += actualQuantity * incoming.getPurchasePrice();
                    quantity -= actualQuantity;
                } else {
                    incoming.setActualQuantity(actualQuantity - quantity);
                    purchasePrice += quantity * incoming.getPurchasePrice();
                    quantity = 0.0;
                }
            }

            totalRevenue += price - purchasePrice;
        }

        LocalDateTime transactionDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Outgoing newOutgoing = Outgoing.builder()
                .user(user)
                .products(outProducts)
                .category(request.getCategory())
                .transactionDate(LocalDateTime.now())
                .comment(request.getComment())
                .price(totalPrice)
                .referenceNumber(getReferenceNumber(transactionDate))
                .revenue(totalRevenue)
                .build();

        outgoingRepository.save(newOutgoing);

        for (OutProduct outProduct:outProducts) {
            Double quantity = incomingRepository.getTotalQuantityByProductId(outProduct.getProduct().getId());

            if (outProduct.getProduct().getThreshold() != null && quantity <= outProduct.getProduct().getThreshold()){
                notificationService.addNewNotification(
                        null,
                        outProduct.getProduct(),
                        null,
                        null,
                        NotificationType.LOW_STOCK);
            }
        }

        return outgoingResponseMapper.apply(newOutgoing);
    }

    @Override
    public List<OutgoingResponse> getOutgoingTransactions(
            Integer storeId,
            List<Integer> userIds,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            List<OutgoingCategory> categories,
            int pageNumber,
            int pageSize,
            Sort.Direction sortOrder
    ){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(sortOrder,"transactionDate")
        );

        List<Outgoing> outgoing = outgoingRepository.getByStoreId(
                store,
                userIds.size() > 0 ? userIds : null,
                productId,
                startDate,
                endDate,
                categories.size() > 0 ? categories : null,
                pageable
        );

        return outgoing.stream().map(outgoingResponseMapper).collect(Collectors.toList());
    }

    private String getReferenceNumber(LocalDateTime transactionDate) {
        return transactionDate.format(DateTimeFormatter.ofPattern("MMddyy"))
                + "-"
                + (outgoingRepository.countByTransactionDate(transactionDate) + 1);
    }
    @Override
    public OutgoingResponse getOutgoingTransaction(Integer id) {
        Optional<Outgoing> outgoingProduct = outgoingRepository.findById(id);
        return outgoingProduct.map(outgoingResponseMapper)
                .orElseThrow(() -> new NotFoundException("Outgoing product not found."));
    }

    private double calculatePrice(Double quantity, Double price){
        return quantity * price;
    }
}
