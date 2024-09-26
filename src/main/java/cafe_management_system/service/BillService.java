package cafe_management_system.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import cafe_management_system.model.Bill;

public interface BillService {

    ResponseEntity<String> generateReport(Map<String, Object> request);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(Map<String, Object> request);

    ResponseEntity<String> deleteBill(Integer id);

}
