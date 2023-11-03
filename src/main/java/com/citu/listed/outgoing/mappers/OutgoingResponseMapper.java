package com.citu.listed.outgoing.mappers;

import com.citu.listed.outgoing.Outgoing;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import com.citu.listed.user.mappers.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OutgoingResponseMapper implements Function<Outgoing, OutgoingResponse> {

    private final UserResponseMapper userResponseMapper;

    @Override
    public OutgoingResponse apply(Outgoing outgoing){
        return new OutgoingResponse(
                outgoing.getId(),
                userResponseMapper.apply(outgoing.getUser()),
                outgoing.getProducts(),
                outgoing.getCategory(),
                outgoing.getPrice(),
                outgoing.getComment(),
                outgoing.getTransactionDate(),
                outgoing.getReferenceNumber()
        );
    }
}
