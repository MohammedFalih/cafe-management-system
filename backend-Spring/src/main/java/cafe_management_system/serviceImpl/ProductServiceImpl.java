package cafe_management_system.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cafe_management_system.JWT.JwtFilter;
import cafe_management_system.JWT.JwtUtil;
import cafe_management_system.constants.CafeConstants;
import cafe_management_system.dao.ProductDao;
import cafe_management_system.model.Category;
import cafe_management_system.model.Product;
import cafe_management_system.service.ProductService;
import cafe_management_system.utils.CafeUtils;
import cafe_management_system.wrapper.ProductWrapper;
import io.jsonwebtoken.Claims;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    JwtUtil jwtUtil;

    private Claims getClaimsFromToken() {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization").substring(7);
        return jwtUtil.extractAllClaims(token);
    }

    private boolean isAdmin(Claims claims) {
        return jwtFilter.isAdmin(claims);
    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> request) {
        Claims claims = getClaimsFromToken();

        if (!isAdmin(claims)) {
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        if (validateProductMap(request, false)) {
            productDao.save(getFromProductMap(request, false));
            return CafeUtils.getResponseEntity("Product added successfully", HttpStatus.OK);
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> request) {
        Claims claims = getClaimsFromToken();

        if (!isAdmin(claims)) {
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        Optional<Product> productOpt = productDao.findById(Integer.parseInt(request.get("id")));
        if (productOpt.isPresent()) {
            Product product = getFromProductMap(request, true);
            product.setStatus(productOpt.get().getStatus());
            productDao.save(product);
            return CafeUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
        }

        return CafeUtils.getResponseEntity("Product Id doesn't exist", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        Claims claims = getClaimsFromToken();

        if (!isAdmin(claims)) {
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        Optional<Product> optional = productDao.findById(id);
        if (optional.isPresent()) {
            productDao.deleteById(id);
            return CafeUtils.getResponseEntity("Product deleted successfully", HttpStatus.OK);
        }

        return CafeUtils.getResponseEntity("Product Id doesn't exist", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> request) {
        Claims claims = getClaimsFromToken();
        try {
            if (isAdmin(claims)) {
                Optional<Product> optional = productDao.findById(Integer.parseInt(request.get("id")));
                if (!optional.isEmpty()) {
                    productDao.updateProductStatus(request.get("status"), Integer.parseInt(request.get("id")));
                    return CafeUtils.getResponseEntity("Product status updated successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product Id doesn't exists.", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductsByCategory(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            ProductWrapper productWrapper = productDao.findProductById(id);
            if (productWrapper != null) {
                return new ResponseEntity<>(productWrapper, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> request, boolean validateToken) {
        if (request.containsKey("name")) {
            if (request.containsKey("id") && validateToken) {
                return true;
            } else if (!validateToken) {
                return true;
            }
        }
        return false;
    }

    private Product getFromProductMap(Map<String, String> request, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(request.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(request.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(request.get("name"));
        product.setDescription(request.get("description"));
        product.setPrice(Integer.parseInt(request.get("price")));
        return product;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllPRoduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

}
