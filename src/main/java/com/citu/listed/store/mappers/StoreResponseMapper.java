package com.citu.listed.store.mappers;

import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.outgoing.OutgoingRepository;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import com.citu.listed.permission.enums.UserPermissions;
import com.citu.listed.product.ProductRepository;
import com.citu.listed.store.Store;
import com.citu.listed.store.dtos.StoreResponse;
import com.citu.listed.user.UserRepository;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StoreResponseMapper implements Function<Store, StoreResponse> {

    private final IncomingRepository incomingRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRepository userRepository;

    @Override
    public StoreResponse apply(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                userResponseMapper.apply(
                        userRepository.findByMemberships_StoreAndMemberships_Permissions_UserPermission(
                                store,
                                UserPermissions.OWNER)
                ),
                store.getStatus(),
                incomingRepository.getTotalProductsByStoreId(store.getId()),
                incomingRepository.getTotalPriceValueByStoreId(store.getId())
        );
    }
}
