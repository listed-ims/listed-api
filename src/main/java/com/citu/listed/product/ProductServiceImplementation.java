package com.citu.listed.product;

import com.citu.listed.shared.exception.BadRequestException;
import com.citu.listed.shared.exception.NotFoundException;
import com.citu.listed.incoming.IncomingRepository;
import com.citu.listed.product.dtos.ProductResponse;
import com.citu.listed.product.enums.ProductFilter;
import com.citu.listed.product.enums.ProductUnit;
import com.citu.listed.product.mappers.ProductResponseMapper;
import com.citu.listed.store.Store;
import com.citu.listed.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final IncomingRepository incomingRepository;
    private final ProductResponseMapper productResponseMapper;

    @Override
    public List<ProductResponse> getProducts(
            Integer storeId,
            String barcode,
            String keyword,
            ProductFilter filter,
            String sort,
            int pageNumber,
            int pageSize
    ) {
        List<Product> products;

        if(barcode.isEmpty()) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sort));

            if(filter == ProductFilter.LOW_STOCK)
                products =  productRepository.getLowStockProductsByStoreId(storeId, pageable);
            else if(filter == ProductFilter.NO_STOCK)
                products = productRepository.getNoStockProductsByStoreId(storeId, pageable);
            else if(filter == ProductFilter.WITH_STOCK)
                products = productRepository.getAvailableProductsByStoreId(storeId, pageable);
            else
                products = productRepository.findByStoreId(storeId, keyword.trim(), pageable);

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
    public ProductResponse addNewProduct(Integer storeId, Product product) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found."));

        if(!validateBarcode(store.getId(), product.getBarcode()))
            throw new BadRequestException("Barcode must be unique.");

        Product newProduct = Product.builder()
                .name(product.getName())
                .barcode(product.getBarcode())
                .variant(product.getVariant())
                .salePrice(product.getSalePrice())
                .threshold(validateThreshold(product.getUnit(), product.getThreshold()))
                .unit(product.getUnit())
                .store(store)
                .build();

        return productResponseMapper.apply(productRepository.save(newProduct));
    }

    @Override
    public ProductResponse updateProduct(Integer id, Product product) {
        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        productToUpdate.setName(product.getName());
        productToUpdate.setBarcode(product.getBarcode());
        productToUpdate.setVariant(product.getVariant());
        productToUpdate.setSalePrice(product.getSalePrice());
        productToUpdate.setThreshold(validateThreshold(productToUpdate.getUnit(), product.getThreshold()));

        return productResponseMapper.apply(productRepository.save(productToUpdate));
    }

    @Override
    public ProductResponse deleteProduct(Integer id) {
        if(incomingRepository.getTotalQuantityByProductId(id) > 0)
            throw new BadRequestException("Product still has stock.");

        Product productToDelete = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        productRepository.deleteById(id);

        return productResponseMapper.apply(productToDelete);
    }

    @Override
    public boolean validateBarcode(Integer storeId, String barcode) {
        if (barcode == null || barcode.isEmpty())
            return true;
        else
            return productRepository.findByStoreIdAndBarcode(storeId, barcode).isEmpty();
    }

    private Double validateThreshold(ProductUnit unit, Double threshold) {
        return threshold != null ?
                (unit == ProductUnit.PCS ? Math.floor(threshold) : threshold) :
                null;
    }
}
