package com.example.quickcode.main.models;

public class ProgrammingLanguageData {

    private final int image;
    private final String name;

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public ProgrammingLanguageData(int image, String name) {
        this.image = image;
        this.name = name;
    }
}
