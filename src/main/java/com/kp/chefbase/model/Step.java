package com.kp.chefbase.model;

public class Step {
    private String stepDescription;
    private String ingredients;
    private String instructions;
    private int cookTime;
    private int flameNumber; // Scale of 1-5
    private String imageUrl;
    private String videoUrl;
    private String notes;
    private String tips;
    private String tools;

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getFlameNumber() {
        return flameNumber;
    }

    public void setFlameNumber(int flameNumber) {
        this.flameNumber = flameNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    @Override
    public String toString() {
        return "Step{" +
                "stepDescription='" + stepDescription + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", cookTime=" + cookTime +
                ", flavorIntensity=" + flameNumber +
                ", imageUrl='" + imageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", notes='" + notes + '\'' +
                ", tips='" + tips + '\'' +
                ", tools='" + tools + '\'' +
                '}';
    }
}
