package com.citu.listed.incoming;

import com.citu.listed.incoming.dtos.IncomingRequest;
import com.citu.listed.incoming.dtos.IncomingResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Incoming")
@RestController
@CrossOrigin
@RequestMapping("/listed/api")
@RequiredArgsConstructor
public class IncomingController {

    private final IncomingService incomingService;

    @PostMapping("/incoming")
    public ResponseEntity<IncomingResponse> inProduct(@RequestHeader HttpHeaders headers,
                                                      @RequestParam Integer productId,
                                                      @RequestBody @Valid IncomingRequest request) {

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);

        IncomingResponse incomingResponse = incomingService.inProduct(token,productId,request);

        return new ResponseEntity<>(incomingResponse, HttpStatus.CREATED);
    }

    @GetMapping("/incoming")
    public ResponseEntity<Object> getIncomingTransactions(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") List<Integer> userIds,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder
    ){
        return new ResponseEntity<>(
                incomingService.getIncomingTransactions(
                        storeId,
                        userIds,
                        productId,
                        startDate,
                        endDate,
                        pageNumber,
                        pageSize,
                        sortOrder
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/incoming/{id}")
    public ResponseEntity<Object> getIncomingTransaction(@PathVariable Integer id){
        return new ResponseEntity<>(incomingService.getIncomingTransaction(id),HttpStatus.OK);
    }

}
