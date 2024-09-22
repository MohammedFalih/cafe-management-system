package cafe_management_system.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cafe_management_system.JWT.JwtFilter;
import cafe_management_system.JWT.JwtUtil;
import cafe_management_system.constants.CafeConstants;
import cafe_management_system.dao.CategoryDao;
import cafe_management_system.model.Category;
import cafe_management_system.service.CategoryService;
import cafe_management_system.utils.CafeUtils;
import io.jsonwebtoken.Claims;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> request) {

        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                .getHeader("Authorization").substring(7);

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            if (jwtFilter.isAdmin(claims)) {
                if (validateCategoryMap(request, false)) {
                    categoryDao.save(getCategoryFromMap(request, false));
                    return CafeUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity("Unauthorized: Only admins can add categories",
                        HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> request, boolean validateId) {
        if (request.containsKey("name")) {
            if (request.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> request, boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(request.get("id")));
        }
        category.setName(request.get("name"));
        return category;
    }

}
