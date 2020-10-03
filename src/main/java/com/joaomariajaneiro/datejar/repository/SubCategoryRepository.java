package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {
    @Query(value = "SELECT u.categories FROM User u WHERE u.username = ?1")
    List<SubCategory> getAllUserPendingCategories(String username);

}
