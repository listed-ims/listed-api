package com.citu.listed.product;

import com.citu.listed.shared.ValidationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/listed/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<Object> getProducts(
            @RequestParam Integer storeId,
            @RequestParam(defaultValue = "") String barcode,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") ProductFilter filter,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                productService.getProducts(storeId, barcode, keyword, filter, sort, pageNumber, pageSize),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> addNewProduct(
            @RequestParam Integer storeId,
            @RequestBody @Valid Product product
    ) {
        return new ResponseEntity<>(productService.addNewProduct(storeId, product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Valid Product product
    ) {
        return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }

    @PostMapping("/validation/barcode")
    public ResponseEntity<Object> getProduct(
            @RequestParam Integer storeId,
            @RequestParam String barcode
    ) {
        return new ResponseEntity<>(
                new ValidationResponse(productService.validateBarcode(storeId, barcode)),
                HttpStatus.OK
        );
    }
}
