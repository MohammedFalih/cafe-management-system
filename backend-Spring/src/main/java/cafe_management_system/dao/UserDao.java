package cafe_management_system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cafe_management_system.model.User;
import cafe_management_system.wrapper.UserWrapper;
import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUser();

    List<String> getAllAdmin();
    
    @Transactional
    @Modifying
    Integer update(@Param("status") String status, @Param("id") Integer id);

}
