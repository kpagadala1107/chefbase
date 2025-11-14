package com.kp.chefbase.repository;

import com.kp.chefbase.model.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
    List<Recipe> findByCategory(String category);
    List<Recipe> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    // MongoDB query methods using method naming conventions
    List<Recipe> findTop10ByOrderByViewCountDesc();

    // For today's recipes, you can use a method with parameters
    @Query("{ 'createdAt' : { $gte: ?0, $lt: ?1 } }")
    List<Recipe> findTodaysRecipes(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    // Alternative: Use method naming for view count with Pageable
    List<Recipe> findAllByOrderByViewCountDesc(Pageable pageable);
}
