package com.kp.chefbase.service;

import com.kp.chefbase.dto.RecipeSummary;
import com.kp.chefbase.model.Recipe;
import com.kp.chefbase.repository.RecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private LLMService llmService = new LLMService();
    public RecipeService(RecipeRepository recipeRepository, LLMService llmService) {
        this.recipeRepository = recipeRepository;
        this.llmService = llmService;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            Recipe foundRecipe = recipe.get();
            // Increment view count
            foundRecipe.setViewCount(foundRecipe.getViewCount() + 1);
            recipeRepository.save(foundRecipe);
            return foundRecipe;
        }
        return null;
    }

    public Recipe createRecipe(Recipe recipe) {
        recipe.setCreatedAt(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    public List<Recipe> createBulkRecipes(List<Recipe> recipes) {
        return recipeRepository.saveAll(recipes);
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

//    public void deleteRecipe(String id, String userId) {
//
//        recipeRepository.deleteById(id);
//    }

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

    public List<Recipe> searchRecipesByName(String recipeName) {
        // Search for recipes where name or description contains the search term (case-insensitive)
        return recipeRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(recipeName, recipeName);
    }

    public List<RecipeSummary> getAllRecipeSummaries() {
        return recipeRepository.findAll().stream()
                .map(recipe -> new RecipeSummary(
                        recipe.getId(),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getCategory(),
                        recipe.getImage(),
                        recipe.getTotalTime(),
                        recipe.getDietaryInfo(),
                        recipe.getUserId(),
                        recipe.getSteps() != null ? recipe.getSteps().size() : 0
                ))
                .collect(Collectors.toList());
    }

    public Recipe updateRecipeWithLLM(String id, String customInstructions) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (existingRecipe.isPresent()) {
            Recipe recipe = existingRecipe.get();
//            if (!recipe.getUserId().equals(userId)) {
//                throw new SecurityException("Not authorized to update this recipe");
//            }

            Recipe updatedRecipe = llmService.updateRecipeWithInstructions(recipe, customInstructions);
//            return recipeRepository.save(updatedRecipe);
            return updatedRecipe;
        }
        return null;
    }

    public List<Recipe> getTopViewedRecipes() {
        Pageable topTen = PageRequest.of(0, 10);
        return recipeRepository.findAllByOrderByViewCountDesc(topTen);
    }

    public List<Recipe> getTodayLatestRecipes() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        Pageable topTen = PageRequest.of(0, 10);
        return recipeRepository.findTodaysRecipes(startOfDay, endOfDay, topTen);
    }



}