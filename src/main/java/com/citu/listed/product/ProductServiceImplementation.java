package com.citu.listed.product;

import com.citu.listed.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductResponseMapper productResponseMapper;

    @Override
    public List<ProductResponse> getProducts(
            Integer storeId,
            String barcode,
            String filter,
            String sort,
            int pageNumber,
            int pageSize
    ) {
        List<Product> products;

        if(barcode.isEmpty()) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sort));
            products = productRepository.findByStoreId(storeId, filter.trim(), pageable);
        } else {
            products = productRepository.findByStoreIdAndBarcode(storeId, barcode);
        }

        return products.stream()
                .map(productResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);

        return product.map(productResponseMapper)
                .orElseThrow(() -> new NotFoundException("Product not found."));
    }

    @Override
    public void addNewProduct(Product product) {
        Product newProduct = Product.builder()
                .name(product.getName())
                .barcode(product.getBarcode())
                .variant(product.getVariant())
                .salePrice(product.getSalePrice())
                .threshold(product.getThreshold())
                .unit(product.getUnit())
                .store(product.getStore())
                .build();

        productRepository.save(newProduct);
    }
}
