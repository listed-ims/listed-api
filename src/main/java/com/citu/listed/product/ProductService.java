package com.citu.listed.product;

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
    void updateProduct(Integer id, Product product);
    boolean validateBarcode(Integer storeId, String barcode);
}
