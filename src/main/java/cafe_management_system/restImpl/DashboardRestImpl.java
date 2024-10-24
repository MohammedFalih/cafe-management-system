package cafe_management_system.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import cafe_management_system.rest.DashboardRest;
import cafe_management_system.service.DashboardService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }

}
