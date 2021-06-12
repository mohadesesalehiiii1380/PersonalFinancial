package com.app.personalfinancial.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Cost {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("amount")
    public String  amount;
    @SerializedName("date")
    public String date;
    @SerializedName("description")
    public String description;
    @SerializedName("category")
    public String category;

    public Cost(String id, Map<String, Object> map) {
        this.id = id;
        this.title = (String) map.get("title");
        this.amount = (String) map.get("amount");
        this.date = (String) map.get("date");
        this.description = (String) map.get("description");
        this.category = (String) map.get("category");
    }
}
