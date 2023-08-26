package com.citu.listed.product;

import com.citu.listed.store.Store;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getProducts(
            Integer storeId,
            String barcode,
            String filter,
            String sort,
            int pageNumber,
            int pageSize
    );
    ProductResponse getProduct(Integer id);
    void addNewProduct(Integer storeId, Product product);
    boolean validateBarcode(Integer storeId, String barcode);
}
