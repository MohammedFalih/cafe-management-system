package cafe_management_system.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cafe_management_system.dao.BillDao;
import cafe_management_system.dao.CategoryDao;
import cafe_management_system.dao.ProductDao;
import cafe_management_system.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> count = new HashMap<>();
        count.put("category", categoryDao.count());
        count.put("product", productDao.count());
        count.put("bill", billDao.count());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
