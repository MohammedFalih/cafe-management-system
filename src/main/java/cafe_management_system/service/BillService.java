package cafe_management_system.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface BillService {

    ResponseEntity<String> generateReport(Map<String, Object> request);

}
