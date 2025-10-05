package com.kp.chefbase.repository;

import com.kp.chefbase.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByCategory(String category);
//    List<Recipe> findByStatus(Activity.Status status);
//    List<Recipe> findByUsersId(String userId);
}