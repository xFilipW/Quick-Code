package com.example.quickcode.main.data;

public class ProgrammingTutorialData {

    private final int image;
    private final String name;
    private final String description;

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProgrammingTutorialData(int image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;
    }
}
