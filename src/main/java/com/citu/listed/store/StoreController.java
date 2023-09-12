package com.citu.listed.store;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStore(
            @RequestHeader HttpHeaders headers,
            @PathVariable Integer id
    ) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        return new ResponseEntity<>(storeService.getStore(token, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> createStore(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid Store store
    ) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring(7);
        return new ResponseEntity<>(storeService.createStore(token, store), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStore(
            @PathVariable Integer id,
            @RequestBody @Valid Store store
    ) {
        return new ResponseEntity<>(storeService.updateStore(id, store), HttpStatus.OK);
    }
}
