package com.example.componentecalculator;

import android.graphics.Bitmap;

public class ComponentImage {
    private String text;
    private Bitmap image;
    private String link;

    public ComponentImage(String text, Bitmap image, String link) {
        this.text = text;
        this.image = image;
        this.link = link;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }
}
