package com.kp.chefbase.rest;

import com.kp.chefbase.exception.RecipeNotFoundException;
import com.kp.chefbase.model.Recipe;
import com.kp.chefbase.model.User;
import com.kp.chefbase.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
        	throw new RecipeNotFoundException("recipe with id - " + id + " not found");
        }
    }

    @GetMapping("/user")
    public List<Recipe> getRecipesByUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return recipeService.getRecipesByUserId(user.getId());
    }

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        recipe.setUserId(user.getId());
        return recipeService.createRecipe(recipe);
    }

    @PostMapping("/list")
    public List<Recipe> createBulkRecipes(@RequestBody List<Recipe> recipes, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        // Set userId for each recipe in the list
        recipes.forEach(recipe -> recipe.setUserId(user.getId()));

        // Save all recipes and return the list
        return recipeService.createBulkRecipes(recipes);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipeDetails, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDetails, user.getId());
        if (updatedRecipe != null) {
            return ResponseEntity.ok(updatedRecipe);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        recipeService.deleteRecipe(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public List<Recipe> getRecipesByCategory(@PathVariable String category) {
        return recipeService.getRecipesByCategory(category);
    }
}