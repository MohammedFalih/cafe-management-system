package cafe_management_system.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/service")
public interface DashboardRest {

    @GetMapping("/details")
    ResponseEntity<Map<String, Object>> getCount();
}
