package com.kp.chefbase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.chefbase.model.Recipe;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LLMService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String model;

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
                .temperature(0.3) // Lower temperature for more consistent, accurate results
                .maxTokens(2500) // Increased for detailed recipes
                .build();

        try {
            ChatCompletionResult result = service.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();

            String jsonResponse = extractJsonFromResponse(response);
            Recipe recipe = objectMapper.readValue(jsonResponse, Recipe.class);

            return recipe;
        } catch (Exception e) {
            System.err.println("Error generating recipe: " + e.getMessage());
            throw new RuntimeException("Failed to generate recipe", e);
        }
    }

    private String extractJsonFromResponse(String response) {
        String cleaned = response.trim();

        // Remove markdown code blocks if present
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
