package com.studio868.cosysharecard;

import android.content.Context;

public final class CosyShareCardBuilder {

    private final Context context;
    private final Context appContext;

    private String header;
    private String title;
    private String description;
    private String sideText;
    private String author;
    private String brand;
    private String shareText;
    private String shareLink;
    private CosyShareCardTheme theme = CosyShareCardTheme.AUTO;

    private float blurRadius = 28f;

    float getBlurRadius() {
        return blurRadius;
    }

    public CosyShareCardBuilder(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }

        this.appContext = context;
        this.context = context.getApplicationContext();
    }

    Context getAppContext() {
        return appContext;
    }

    public CosyShareCardBuilder setShareLink(String shareLink) {
        this.shareLink = shareLink;
        return this;
    }

    String getShareLink() {
        return shareLink;
    }

    public CosyShareCardBuilder setHeader(String header) {
        this.header = header;
        return this;
    }

    public CosyShareCardBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public CosyShareCardBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CosyShareCardBuilder setSideText(String sideText) {
        this.sideText = sideText;
        return this;
    }

    public CosyShareCardBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public CosyShareCardBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public CosyShareCardBuilder setShareText(String shareText) {
        this.shareText = shareText;
        return this;
    }

    String getShareText() {
        return shareText;
    }


    public CosyShareCardBuilder setBlurRadius(float blurRadius) {
        if (blurRadius < 0) {
            throw new IllegalArgumentException("Blur radius cannot be negative.");
        }

        this.blurRadius = blurRadius;
        return this;
    }

    public CosyShareCardBuilder setTheme(CosyShareCardTheme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Theme cannot be null.");
        }

        this.theme = theme;
        return this;
    }

    CosyShareCardTheme getTheme() {
        return theme;
    }

    Context getContext() {
        return context;
    }

    String getHeader() { return header; }
    String getTitle() { return title; }
    String getDescription() { return description; }
    String getSideText() { return sideText; }
    String getAuthor() { return author; }
    String getBrand() { return brand; }

}
