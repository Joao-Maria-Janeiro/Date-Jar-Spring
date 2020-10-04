package com.joaomariajaneiro.datejar.bootstrap;

import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.model.enums.Type;
import com.joaomariajaneiro.datejar.repository.CategoryRepository;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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


    public void importData() {
        log.info("Dummy Data - Initializing...");

        List<Category> categories = new ArrayList<>();


        categories.add(
                new Category(
                        "Restaurant",
                        Type.DINNING
                )
        );
        categories.add(
                new Category(
                        "Sight Seeing",
                        Type.TRAVEL
                )
        );
        categories.add(
                new Category(
                        "Adventure",
                        Type.ADVENTURE
                )
        );
        categoryRepository.saveAll(categories);


        userRepository.save(
                new User(
                        "admin", "admin@admin.com", "admin", categories
                )
        );

        log.info("Dummy Data - Initialized!");

    }
}
