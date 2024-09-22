package cafe_management_system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe_management_system.model.Category;

public interface CategoryDao extends JpaRepository<Category, Integer>{

    List<Category> getAllCategory();
}
