package cafe_management_system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import cafe_management_system.model.Product;
import cafe_management_system.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductWrapper> getProductsByCategory(@Param("id") Integer id);

    ProductWrapper findProductById(@Param("id") Integer id);

}
