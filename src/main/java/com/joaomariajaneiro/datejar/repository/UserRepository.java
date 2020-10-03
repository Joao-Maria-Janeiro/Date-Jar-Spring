package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT u.categories FROM User u WHERE u.id = ?1")
    List<Category> getAllUserPendingCategories(Long id);

    @Query(value = "SELECT u.categories FROM User u WHERE u.username = ?1")
    List<Category> getAllUserPendingCategories(String username);

    User getById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);
}
