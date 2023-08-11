package com.citu.listed.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("")
    public ResponseEntity<Object> getProducts(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") String barcode,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                productService.getProducts(storeId, barcode, filter, sort, pageNumber, pageSize),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }
}
