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

    @PostMapping("")
    public ResponseEntity<Object> createStore(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody @Valid Store store
    ) {
        storeService.createStore(authorization.substring(7), store);
        return new ResponseEntity<>("Store created.", HttpStatus.CREATED);
    }

}
