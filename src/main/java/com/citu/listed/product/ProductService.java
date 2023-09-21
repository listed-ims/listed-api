package com.citu.listed.product;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getProducts(
            Integer storeId,
            String barcode,
            String keyword,
            ProductFilter filter,
            String sort,
            int pageNumber,
            int pageSize
    );
    ProductResponse getProduct(Integer id);
    ProductResponse addNewProduct(Integer storeId, Product product);
    ProductResponse updateProduct(Integer id, Product product);
    ProductResponse deleteProduct(Integer id);
    boolean validateBarcode(Integer storeId, String barcode);
}
