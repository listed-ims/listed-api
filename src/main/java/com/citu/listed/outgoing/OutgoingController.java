package com.citu.listed.outgoing;

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
                                              @RequestBody @Valid Outgoing outgoing){

        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);

        OutgoingResponse outgoingResponse = outgoingService.outProducts(token, outgoing);

        return new ResponseEntity<>(outgoingResponse, HttpStatus.CREATED);

    }
}
