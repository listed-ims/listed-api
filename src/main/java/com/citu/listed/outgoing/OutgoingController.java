package com.citu.listed.outgoing;

import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import com.citu.listed.outgoing.enums.OutgoingCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/listed/api")
@RequiredArgsConstructor
public class OutgoingController {

    private final OutgoingService outgoingService;

    @PostMapping("/outgoing")
    public ResponseEntity<Object> outProducts(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid OutgoingRequest request){

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);

        OutgoingResponse outgoingResponse = outgoingService.outProducts(token, request);

        return new ResponseEntity<>(outgoingResponse, HttpStatus.CREATED);
    }

    @GetMapping("/outgoing")
    public ResponseEntity<Object> getOutgoingTransactions(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") List<Integer> userIds,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "") List<OutgoingCategory> categories,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder
    ){
        return new ResponseEntity<>(
                outgoingService.getOutgoingTransactions(
                        storeId,
                        userIds,
                        productId,
                        startDate,
                        endDate,
                        categories,
                        pageNumber,
                        pageSize,
                        sortOrder
                )
                ,HttpStatus.OK);
    }
    
    @GetMapping("/outgoing/{id}")
    public ResponseEntity<Object> getOutgoingTransaction(@PathVariable Integer id){
        return new ResponseEntity<>(outgoingService.getOutgoingTransaction(id),HttpStatus.OK);
    }
}
