package com.citu.listed.store;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("")
    public ResponseEntity<Object> getStores(
            @RequestHeader HttpHeaders headers,
            @RequestParam(defaultValue = "") StoreStatus status,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                storeService.getStores(
                        headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7),
                        status,
                        pageNumber,
                        pageSize
                        ),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<Object> createStore(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid Store store
    ) {
        storeService.createStore(headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7), store);
        return new ResponseEntity<>("Store created.", HttpStatus.CREATED);
    }

}
