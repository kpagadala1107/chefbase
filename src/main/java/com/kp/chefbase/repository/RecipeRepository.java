package com.kp.chefbase.repository;

import com.kp.chefbase.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
    List<Recipe> findByCategory(String category);
    List<Recipe> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

}
