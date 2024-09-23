package cafe_management_system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe_management_system.model.Product;
import cafe_management_system.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();


}
