package com.kp.chefbase.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.chefbase.model.Recipe;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class LLMService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${azure.storage.connection-string}")
    private String azureStorageConnectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Recipe generateRecipe(String recipeName) {
        OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(120));

        String prompt = String.format(
                "Generate an authentic, traditional recipe for %s. Please ensure accuracy by referencing authentic culinary sources and traditional cooking methods. " +
                        "Include proper ingredient quantities, cooking techniques, and cultural context where applicable. " +
                        "Format the response as JSON with this exact structure with specifying ingredients specific to that step:\n\n" +
                        "{\n" +
                        "  \"name\": \"Recipe Name\",\n" +
                        "  \"description\": \"Detailed description including origin and cultural significance\",\n" +
                        "  \"category\": \"Main Course/Appetizer/Dessert/etc\",\n" +
                        "  \"image\": \"https://example.com/image.jpg\",\n" +
                        "  \"totalTime\": 60,\n" +
                        "  \"dietaryInfo\": \"Detailed dietary information, allergens, and dietary restrictions\",\n" +
                        "  \"steps\": [\n" +
                        "    {\n" +
                        "      \"stepDescription\": \"Step title\",\n" +
                        "      \"ingredients\": \"Specific ingredients with exact quantities\",\n" +
                        "      \"instructions\": \"Detailed step-by-step instructions with techniques\",\n" +
                        "      \"cookTime\": 15,\n" +
                        "      \"flameNumber\": 3,\n" +
                        "      \"imageUrl\": null,\n" +
                        "      \"videoUrl\": null,\n" +
                        "      \"notes\": \"Important tips and variations\",\n" +
                        "      \"tips\": \"Professional cooking tips\",\n" +
                        "      \"tools\": \"Required cooking tools and equipment\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n" +
                        "\nEnsure all measurements are precise, cooking times are accurate, and techniques are traditionally correct.",
                recipeName
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(new ChatMessage("user", prompt)))
                .temperature(0.3)
                .maxTokens(2500)
                .build();

        try {
            ChatCompletionResult result = service.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();

            String jsonResponse = extractJsonFromResponse(response);
            Recipe recipe = objectMapper.readValue(jsonResponse, Recipe.class);

            // Generate and upload image
//            String imageUrl = generateAndUploadRecipeImage(service, recipe.getName());
//            recipe.setImage(imageUrl);

            return recipe;
        } catch (Exception e) {
            System.err.println("Error generating recipe: " + e.getMessage());
            throw new RuntimeException("Failed to generate recipe", e);
        }
    }

    public Recipe updateRecipeWithInstructions(Recipe existingRecipe, String customInstructions) {
        OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(120));

        String prompt = String.format(
                "I have an existing recipe that needs to be updated based on specific instructions. " +
                        "Here is the current recipe:\n\n%s\n\n" +
                        "Please update this recipe according to these instructions: %s\n\n" +
                        "Maintain the authenticity and accuracy of the recipe while incorporating the requested changes. " +
                        "Return the updated recipe in the same JSON format with this exact structure:\n\n" +
                        "{\n" +
                        "  \"name\": \"Recipe Name\",\n" +
                        "  \"description\": \"Updated description\",\n" +
                        "  \"category\": \"Category\",\n" +
                        "  \"image\": \"current image URL\",\n" +
                        "  \"totalTime\": totalTimeInMinutes,\n" +
                        "  \"dietaryInfo\": \"Updated dietary information\",\n" +
                        "  \"steps\": [\n" +
                        "    {\n" +
                        "      \"stepDescription\": \"Step title\",\n" +
                        "      \"ingredients\": \"Ingredients with quantities\",\n" +
                        "      \"instructions\": \"Detailed instructions\",\n" +
                        "      \"cookTime\": timeInMinutes,\n" +
                        "      \"flameNumber\": flameLevel,\n" +
                        "      \"imageUrl\": null,\n" +
                        "      \"videoUrl\": null,\n" +
                        "      \"notes\": \"Important notes\",\n" +
                        "      \"tips\": \"Cooking tips\",\n" +
                        "      \"tools\": \"Required tools\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}",
                convertRecipeToString(existingRecipe),
                customInstructions
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(new ChatMessage("user", prompt)))
                .temperature(0.3)
                .maxTokens(2500)
                .build();

        try {
            ChatCompletionResult result = service.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();

            String jsonResponse = extractJsonFromResponse(response);
            Recipe updatedRecipe = objectMapper.readValue(jsonResponse, Recipe.class);

            // Preserve original recipe metadata
            updatedRecipe.setId(existingRecipe.getId());
            updatedRecipe.setUserId(existingRecipe.getUserId());

            return updatedRecipe;
        } catch (Exception e) {
            System.err.println("Error updating recipe: " + e.getMessage());
            throw new RuntimeException("Failed to update recipe", e);
        }
    }

    private String convertRecipeToString(Recipe recipe) {
        try {
            return objectMapper.writeValueAsString(recipe);
        } catch (Exception e) {
            return recipe.toString();
        }
    }

    private String generateAndUploadRecipeImage(OpenAiService service, String recipeName) {
        try {
            // Generate image using DALL-E
            String imagePrompt = String.format(
                    "A high-quality, appetizing photo of %s, professionally plated on a clean white plate, " +
                            "natural lighting, food photography style, vibrant colors, restaurant quality presentation",
                    recipeName
            );

            CreateImageRequest imageRequest = CreateImageRequest.builder()
                    .prompt(imagePrompt)
                    .model("dall-e-3")
                    .size("1024x1024")
                    .quality("standard")
                    .n(1)
                    .build();

            ImageResult imageResult = service.createImage(imageRequest);
            String generatedImageUrl = imageResult.getData().get(0).getUrl();

            // Download image and upload to Azure Blob Storage
            return uploadImageToAzure(generatedImageUrl, recipeName);

        } catch (Exception e) {
            System.err.println("Error generating recipe image: " + e.getMessage());
            return null; // Return null if image generation fails
        }
    }

    private String uploadImageToAzure(String imageUrl, String recipeName) throws IOException {
        // Create blob service client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureStorageConnectionString)
                .buildClient();

        // Get container client
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Create container if it doesn't exist
        if (!containerClient.exists()) {
            containerClient.create();
        }

        // Generate unique blob name
        String blobName = "recipe-" + UUID.randomUUID() + "-" +
                recipeName.toLowerCase().replaceAll("[^a-z0-9]", "-") + ".jpg";

        // Download image from OpenAI URL
        try (InputStream imageStream = new URL(imageUrl).openStream()) {
            byte[] imageBytes = imageStream.readAllBytes();

            // Upload to Azure Blob Storage
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(new ByteArrayInputStream(imageBytes), imageBytes.length, true);

            // Return the Azure blob URL
            return blobClient.getBlobUrl();
        }
    }

    private String extractJsonFromResponse(String response) {
        String cleaned = response.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        }
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        int startIndex = cleaned.indexOf("{");
        int endIndex = cleaned.lastIndexOf("}") + 1;

        if (startIndex >= 0 && endIndex > startIndex) {
            return cleaned.substring(startIndex, endIndex);
        }

        return response;
    }
}
