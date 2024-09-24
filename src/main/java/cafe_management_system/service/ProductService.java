package cafe_management_system.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import cafe_management_system.wrapper.ProductWrapper;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, String> request);

    ResponseEntity<List<ProductWrapper>> getAllPRoduct();

    ResponseEntity<String> updateProduct(Map<String, String> request);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> request);

    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);

}
