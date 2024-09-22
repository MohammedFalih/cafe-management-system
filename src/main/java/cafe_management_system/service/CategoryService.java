package cafe_management_system.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import cafe_management_system.model.Category;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> request);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> request);
}
