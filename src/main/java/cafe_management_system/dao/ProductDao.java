package cafe_management_system.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe_management_system.model.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

}
