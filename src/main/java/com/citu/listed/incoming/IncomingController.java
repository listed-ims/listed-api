package com.citu.listed.incoming;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getIncomingTransactions(@RequestParam Integer storeId){
        return new ResponseEntity<>(incomingService.getIncomingTransactions(storeId),HttpStatus.OK);
    }

}
