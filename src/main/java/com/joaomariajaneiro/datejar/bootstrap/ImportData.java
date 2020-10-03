package com.joaomariajaneiro.datejar.bootstrap;

import com.joaomariajaneiro.datejar.model.SubCategory;
import com.joaomariajaneiro.datejar.repository.SubCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.model.enums.Type;
import com.joaomariajaneiro.datejar.repository.CategoryRepository;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ImportData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;


    public void importData() {
        log.info("Dummy Data - Initializing...");

        List<Category> categories = new ArrayList<>();
        List<SubCategory> subCategories = new ArrayList<>();

        categories.add(
                new Category(
                        "Restaurant"
                )
        );

        Category sight_seeing = new Category(
                "Sight Seeing"
        );

        categories.add(
                sight_seeing
        );

        categories.add(
                new Category(
                        "Adventure"
                )
        );

        SubCategory future_trips = new SubCategory(
                "Future Trips",
                "image-test-test"
        );

        sight_seeing.addSubCategory(
                future_trips

        );

        SubCategory next_trips = new SubCategory(
                "Next Trips",
                "image-test-test"
        );

        sight_seeing.addSubCategory(
                next_trips

        );

        subCategories.add(future_trips);
        subCategories.add(next_trips);

        subCategoryRepository.saveAll(subCategories);
        categoryRepository.saveAll(categories);


        userRepository.save(
                new User(
                        "admin", "admin@admin.com", "admin", categories
                )
        );

        log.info("Dummy Data - Initialized!");

    }
}
