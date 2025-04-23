package com.example.luxevista_app;

public class Room_Info {
    private String title;
    private String description;
    private String price;
    private int imageResId;
    private String roomType; // e.g., "Suite", "Deluxe", "Standard", "Villa"
    private boolean isAvailable;
    private double priceValue; // Numeric value for sorting/filtering

    public Room_Info(String title, String description, String price, int imageResId,
                     String roomType, boolean isAvailable) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.roomType = roomType;
        this.isAvailable = isAvailable;

        // Extract numeric price value from price string (e.g., "$250 / night" -> 250.0)
        try {
            String priceStr = price.replaceAll("[^0-9.]", "");
            this.priceValue = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            this.priceValue = 0.0;
        }
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getRoomType() { return roomType; }
    public boolean isAvailable() { return isAvailable; }
    public double getPriceValue() { return priceValue; }
}