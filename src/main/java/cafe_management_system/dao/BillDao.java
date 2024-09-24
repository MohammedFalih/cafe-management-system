package cafe_management_system.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe_management_system.model.Bill;

public interface BillDao extends JpaRepository<Bill, Integer> {

}
