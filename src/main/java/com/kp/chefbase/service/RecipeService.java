package com.kp.chefbase.service;

import com.kp.chefbase.model.Recipe;
import com.kp.chefbase.model.User;
import com.kp.chefbase.repository.RecipeRepository;
import com.kp.chefbase.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe createRecipe(Recipe recipe) {
    	List<User> users = userRepository.findByAlertsForCategoriesContaining(recipe.getCategory());
    	System.out.println(users);
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(String id, Recipe recipeDetails) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe != null) {
            recipe.setName(recipeDetails.getName());
            recipe.setDescription(recipeDetails.getDescription());
            recipe.setCategory(recipeDetails.getCategory());
            recipe.setSteps(recipeDetails.getSteps());

            return recipeRepository.save(recipe);
        }
        return null;
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

//    public List<Recipe> getRecipesByStatus(Recipe.Status status) {
//        return recipeRepository.findByStatus(status);
//    }
}