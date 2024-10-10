package com.example.test6;

// IM/2021/104 Start
public class addRecipeClass {
    String recipeID;
    String name;
    String ingredients;
    String method;
    String duration;
    String category;
    String userId;
    String imgUrl;
    String videoUrl;
    boolean favorite;



    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category, String userId) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
        this.userId = userId;
    }

    // constructor with all parameters
    public addRecipeClass(String recipeID, String name, String ingredients, String method, String duration, String category, String userId, String imgUrl, String videoUrl, boolean favorite) {
        this.recipeID = recipeID;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
        this.duration = duration;
        this.category = category;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.favorite = favorite;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

//IM/2021/104 End

