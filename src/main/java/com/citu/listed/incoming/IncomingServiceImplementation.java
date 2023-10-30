package com.citu.listed.incoming;

import com.citu.listed.shared.exception.BadRequestException;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.incoming.dtos.IncomingRequest;
import com.citu.listed.incoming.dtos.IncomingResponse;
import com.citu.listed.incoming.mappers.IncomingResponseMapper;
import com.citu.listed.product.Product;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import com.citu.listed.user.User;
import com.citu.listed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.citu.listed.user.config.JwtService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomingServiceImplementation implements IncomingService {

    private final IncomingRepository incomingRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final JwtService jwtService;
    private final IncomingResponseMapper incomingResponseMapper;

    @Override
    public IncomingResponse inProduct(String token, Integer productId, IncomingRequest request) {

        User user = userRepository.findByUsername(jwtService.extractUsername(token))
                .orElseThrow(() -> new NotFoundException("User not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        if (request.getPurchasePrice() > product.getSalePrice()) {
            throw new BadRequestException("Sale price must be greater than purchase price.");
        }

        LocalDateTime transactionDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Incoming newIncoming = Incoming.builder()
                .product(product)
                .user(user)
                .initialQuantity(request.getInitialQuantity())
                .actualQuantity(request.getInitialQuantity())
                .purchasePrice(request.getPurchasePrice())
                .expirationDate(request.getExpirationDate())
                .transactionDate(transactionDate)
                .comment(request.getComment())
                .referenceNumber(getReferenceNumber(transactionDate))
                .build();

       incomingRepository.save(newIncoming);

       return incomingResponseMapper.apply(newIncoming);
    }

    @Override
    public List<IncomingResponse> getIncomingTransactions(
            Integer storeId,
            List<Integer> userIds,
            Integer productId,
            LocalDate startDate,
            LocalDate endDate,
            int pageNumber,
            int pageSize,
            Sort.Direction sortOrder
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(sortOrder,"transactionDate")
        );

        List<Incoming> incoming = incomingRepository.getByStoreId(
                store,
                userIds.size() > 0 ? userIds : null,
                productId,
                startDate,
                endDate,
                pageable
        );

        return incoming.stream().map(incomingResponseMapper).collect(Collectors.toList());
    }

    @Override
    public IncomingResponse getIncomingTransaction(Integer id){
        Optional<Incoming> incoming = incomingRepository.findById(id);

        return incoming.map(incomingResponseMapper)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
    }

    private String getReferenceNumber(LocalDateTime transactionDate) {
        return transactionDate.format(DateTimeFormatter.ofPattern("MMddyy"))
                + "-"
                + (incomingRepository.countByTransactionDate(transactionDate) + 1);
    }
}
