package com.kp.chefbase.service;

import com.kp.chefbase.model.Recipe;
import com.kp.chefbase.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(String id, Recipe recipeDetails, String userId) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe recipe = existingRecipe.get();
            if (!recipe.getUserId().equals(userId)) {
                throw new SecurityException("Not authorized to update this recipe");
            }
            recipe.setName(recipeDetails.getName());
            recipe.setCategory(recipeDetails.getCategory());
            recipe.setDescription(recipeDetails.getDescription());
            recipe.setImage(recipeDetails.getImage());
            recipe.setTotalTime(recipeDetails.getTotalTime());
            recipe.setDietaryInfo(recipeDetails.getDietaryInfo());
            recipe.setSteps(recipeDetails.getSteps());
            return recipeRepository.save(recipe);
        }
        return null;
    }

    public void deleteRecipe(String id, String userId) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe recipe = existingRecipe.get();
            if (!recipe.getUserId().equals(userId)) {
                throw new SecurityException("Not authorized to delete this recipe");
            }
        }
        recipeRepository.deleteById(id);
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategory(category);
    }

    public List<Recipe> getRecipesByUserId(String id) {
        return recipeRepository.findByUserId(id);
    }

}