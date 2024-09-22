package cafe_management_system.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> request );
}
