package cafe_management_system.rest;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cafe_management_system.wrapper.ProductWrapper;

@RequestMapping("/product")
public interface ProductRest {

    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> request);

    @GetMapping("/get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct();

    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String, String> request);
}
