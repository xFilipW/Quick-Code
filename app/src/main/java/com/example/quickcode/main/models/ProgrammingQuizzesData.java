package com.example.quickcode.main.models;

public class ProgrammingQuizzesData {

    private final int image;
    private final String title;
    private final String description;

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ProgrammingQuizzesData(int image, String name, String description) {
        this.image = image;
        this.title = name;
        this.description = description;
    }
}
