package cafe_management_system.rest;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> request);

    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);
}
