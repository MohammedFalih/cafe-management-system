package cafe_management_system.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cafe_management_system.constants.CafeConstants;
import cafe_management_system.rest.ProductRest;
import cafe_management_system.service.ProductService;
import cafe_management_system.utils.CafeUtils;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired 
    ProductService productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> request) {
        try {   
            return productService.addNewProduct(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
