package com.citu.listed.outgoing;

import com.citu.listed.outgoing.dtos.OutgoingRequest;
import com.citu.listed.outgoing.dtos.OutgoingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping("/outgoing/{id}")
    public ResponseEntity<Object> getOutgoingTransaction(@PathVariable Integer id){
        return new ResponseEntity<>(outgoingService.getOutgoingTransaction(id),HttpStatus.OK);
    }
}
