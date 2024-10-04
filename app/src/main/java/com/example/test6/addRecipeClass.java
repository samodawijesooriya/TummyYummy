package com.example.test6;

public class addRecipeClass {
    String recipeID;
    String name;
    String ingredients;
    String method;
    String duration;
    String category;
    String userId;
    String imgUrl;



    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category, String userId, String imgUrl) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
        this.userId = userId;
        this.imgUrl = imgUrl;
    }

    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category, String userId) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
        this.userId = userId;
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

