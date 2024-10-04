package com.example.test6;

public class addRecipeClass {
    String recipeID;
    String name;
    String ingredients;
    String method;
    String duration;
    String category;
    String imageUrl;

    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category, String imageUrl) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
    }

    public addRecipeClass() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
