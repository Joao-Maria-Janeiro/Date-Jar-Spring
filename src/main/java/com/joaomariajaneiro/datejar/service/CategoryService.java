package com.joaomariajaneiro.datejar.service;

import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Map<String, List<Category>> getAllUserPendingCategories(String username) {
        List<Category> allUserPendingCategories =
                categoryRepository.getAllUserPendingCategories(username);

        Map<String, List<Category>> typesToCategories = new HashMap<>();
        allUserPendingCategories.forEach(
                category -> {
                    if (typesToCategories.containsKey(category.getType().name())) {
                        List<Category> categories =
                                typesToCategories.get(category.getType().name());
                        categories.add(category);
                        typesToCategories.put(category.getType().name(), categories);
                    } else {
                        List<Category> categories = new ArrayList<>();
                        categories.add(category);
                        typesToCategories.put(category.getType().name(), categories);
                    }
                }
        );
        return typesToCategories;
    }

    public int save(Category category, String username) {
        return categoryRepository.save(category, username);
    }

    public int updateName(String newCategoryName, int categoryId, String username) {
        return categoryRepository.updateName(newCategoryName, categoryId, username);
    }

    public void removeCategory(Long categoryId, String username) throws Exception {
        try {
            categoryRepository.removeActivitiesFromCategory(categoryId, username);
            categoryRepository.remove(categoryId, username);
        } catch (Exception e) {
            throw e;
        }

    }
}
