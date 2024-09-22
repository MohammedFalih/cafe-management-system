package cafe_management_system.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, String> request);

}
