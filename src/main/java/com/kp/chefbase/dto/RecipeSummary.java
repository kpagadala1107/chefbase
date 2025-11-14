package com.kp.chefbase.dto;

public class RecipeSummary {
    private String id;
    private String name;
    private String description;
    private String category;
    private String image;
    private int totalTime;
    private String dietaryInfo;
    private String userId;
    private int stepsCount;

    // Constructors
    public RecipeSummary() {}

    public RecipeSummary(String id, String name, String description, String category,
                         String image, int totalTime, String dietaryInfo, String userId, int stepsCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.totalTime = totalTime;
        this.dietaryInfo = dietaryInfo;
        this.userId = userId;
        this.stepsCount = stepsCount;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }
    public String getDietaryInfo() { return dietaryInfo; }
    public void setDietaryInfo(String dietaryInfo) { this.dietaryInfo = dietaryInfo; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getStepsCount() { return stepsCount; }
    public void setStepsCount(int stepsCount) { this.stepsCount = stepsCount; }
}
